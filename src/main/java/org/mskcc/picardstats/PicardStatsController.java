package org.mskcc.picardstats;

import org.mskcc.picardstats.model.*;
import org.mskcc.picardstats.repository.*;
import org.mskcc.sequencer.SequencerDoneController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class PicardStatsController {
    private static Logger log = LoggerFactory.getLogger(SequencerDoneController.class);

    private static final String BASE_STATS_DIR = "/ifs/data/BIC/Stats/hiseq/DONE/";  // "/Users/mcmanamd/Downloads/DONE/DONE/";

    private static final String [] ACTIVE_SEQUENCERS =
            {"DIANA", "JAX", "JOHNSAWYERS", "KIM", "LIZ", "MICHELLE", "MOMO", "PITT", "SCOTT", "TOMS", "VIC"};

    private static final String [] ALL_SEQUENCERS =
            {"BRAD", "DIANA", "JAX", "JOHNSAWYERS", "KIM", "LIZ", "LOLA", "MICHELLE", "MOMO", "PITT", "SCOTT", "TOMS", "VIC"};

    @Autowired
    private WgsMetricsRepository wgsRepository;
    @Autowired
    private CpcgMetricsRepository cpcgRepository;
    @Autowired
    private QMetricsRepository qMetricsRepository;
    @Autowired
    private RnaSeqMetricsRepository rnaSeqRepository;
    @Autowired
    private DuplicationMetricsRepository dmRepository;
    @Autowired
    private HsMetricsRepository hsRepository;
    @Autowired
    private AlignmentMetricsRepository amRepository;
    @Autowired
    private PicardFileRepository picardFileRepository;


    @RequestMapping(value = "*", method = RequestMethod.GET)
    @ResponseBody
    public String getFallback() {
        try {
            // TODO call from new endpoint & write output to /lims/stats/run_reports
            //currently written to /ifs/data/bio/LIMS/stats/run_reports/ /ifs/data/bio/LIMS/stats/project_reports/
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
            String date = simpleDateFormat.format(new Date());

            String request = "09377_B";
            File projectFileName =  new File("AutoReport_P" + request + "_" + date + ".xls");
            PicardToExcel.writeExcel(projectFileName, picardFileRepository.findByRequest(request));

            String run = "JOHNSAWYERS_0205_000000000-G3N9B";
            File runFileName =  new File("AutoReport_" + run + "_" + date + ".xls");
            PicardToExcel.writeExcel(runFileName, picardFileRepository.findByRun(run));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Fallback for Picard Stats GET Requests";
    }

    /**
     * Returns the Picard stats for the pooled normal samples if they were added to the run.
     * @param runId
     * @return
     */
    @RequestMapping(value = "/picardstats-controls/run/{runId}", method = RequestMethod.GET)
    public List<QCSiteStats> getQCSiteStats(@PathVariable String runId) {
        System.out.println("Querying pooled normals for run: " + runId);
        long startTime = System.currentTimeMillis();

        List<PicardFile> files = picardFileRepository.findByRunAndRequest(runId, "POOLEDNORMALS");
        if (files == null || files.size() == 0) {
            System.out.println("No data found for run: " + runId);
            return null;
        }

        Map<String, QCSiteStats> statsMap = convertPicardFilesToQcStats(files);

        System.out.println("Elapsed time pooled normal stats queries (ms): " + (System.currentTimeMillis() - startTime));
        List<QCSiteStats> result = new ArrayList(statsMap.values());
        return result;
    }

    public Map<String, QCSiteStats> convertPicardFilesToQcStats(List<PicardFile> files) {
        Map<String, QCSiteStats> statsMap = new HashMap<>(); // Run-Request-Sample to QCSiteStats
        for (PicardFile pf : files) {
            if (!pf.isParseOK())
                continue;

            QCSiteStats stats = statsMap.getOrDefault(pf.getMd5RRS(), new QCSiteStats(pf));

            switch (pf.getFileType()) {
                case "AM":
                    List<AlignmentSummaryMetrics> am = amRepository.findByFilename(pf.getFilename());
                    for (AlignmentSummaryMetrics x : am) {
                        if (x.CATEGORY == AlignmentSummaryMetrics.Category.UNPAIRED ||
                                x.CATEGORY == AlignmentSummaryMetrics.Category.PAIR)
                            stats.addAM(x);
                    }
                    break;
                case "MD":
                    List<DuplicationMetrics> dm = dmRepository.findByFilename(pf.getFilename());
                    stats.addDM(dm.get(0));
                    break;
                case "WGS":
                    List<WgsMetrics> wgs = wgsRepository.findByFilename(pf.getFilename());
                    stats.addWGS(wgs.get(0));
                    break;
                case "HS":
                    List<HsMetrics> hs = hsRepository.findByFilename(pf.getFilename());
                    stats.addHS(hs.get(0));
                    break;
                case "mskQ":
                    List<QMetric> qMetrics = qMetricsRepository.findByFilename(pf.getFilename());
                    stats.addQ(qMetrics.get(0));
                    break;
                case "RNA":
                    List<RnaSeqMetrics> rnaSeqMetrics = rnaSeqRepository.findByFilename(pf.getFilename());
                    stats.addRna(rnaSeqMetrics.get(0));
                    break;
                case "oxoG":
                    List<CpcgMetrics> cpcgMetrics = cpcgRepository.findByFilename(pf.getFilename());
                    stats.addCpcg(cpcgMetrics.get(0));
                    break;
                default:
                    System.out.println("File type ignored by QC site:" + pf.getFileType());
            }
            statsMap.put(pf.getMd5RRS(), stats);
        }
        return statsMap;
    }

    @GetMapping(value = "/picardstats/update/{nDays}")
    /*
    Without a messaging system to trigger updates to specific files we can list all files written in the last n days and check if they
    are in the database.  Unfortunately, the RAID is very slow so this process takes ~3 minutes for MICHELLE only
     */
    public String updateDatabase(@PathVariable Integer nDays) throws Exception {
        System.out.println("Updating the database for files written in the past " + nDays + " day(s).");
        long startTime = System.currentTimeMillis(); // the NY Isilon RAID is slow so tack elapsed time

        File baseDir = new File(BASE_STATS_DIR);
        if (!baseDir.exists()) {
            String msg = "Base directory does not exist: " + BASE_STATS_DIR;
            System.err.println(msg);
            return "ERROR: " + msg;
        }

        // for each active sequencer, list new files created within last n days
        for (String sequencer : ACTIVE_SEQUENCERS) {
            File f = new File(BASE_STATS_DIR + sequencer);
            File [] statsFiles = f.listFiles(new DaysFileFilter(nDays));
            long elapsedTime = System.currentTimeMillis() - startTime;
            System.out.println("Processing sequencer: " + sequencer + " files to process: " + statsFiles.length + ", Elapsed time (ms): " + elapsedTime);
            for (File statsFile: statsFiles) {
                saveStats(statsFile, true);
            }
        }

        String msg = "DONE. Elapsed time (ms): " + (System.currentTimeMillis() - startTime);
        System.out.println(msg);
        return msg;
    }

    /**
     * Parse Picard files for a specific Sequencer/Run pair
     */
    @GetMapping(value = "/picardstats/updaterun/{sequencer}/{run}")
    public String updateDatabaseByRun(@PathVariable String sequencer, @PathVariable String run) throws Exception {
        if (sequencer.contains(" ") || run.contains(" "))
            return "Error";

        FilenameFilter prefixFilter = (dir, name) -> {
            if (name.startsWith(run)) {
                return true;
            } else {
                return false;
            }
        };

        File statsDir = new File(BASE_STATS_DIR + sequencer);
        File [] statsFiles = statsDir.listFiles(prefixFilter);
        for (File statsFile: statsFiles) {
            saveStats(statsFile, true);
        }
        return "Files parsed: " + statsFiles.length;
    }

    @GetMapping(value = "/picardstats/run/{runId}")
    public Map<String, QCSiteStats> getPicardStatsByRunId(@PathVariable String runId) {
        List<PicardFile> picardFiles = picardFileRepository.findByRun(runId);

        Map<String, QCSiteStats> qcSiteStatsMap = convertPicardFilesToQcStats(picardFiles);

        return qcSiteStatsMap;
    }

    @GetMapping(value = "/picardstats/run/{runId}/sample/{sampleId}")
    public Map<String, QCSiteStats> getPicardStatsByRunIdAndSample(@PathVariable String runId,
                                                                   @PathVariable String sampleId) {
        List<PicardFile> picardFiles = picardFileRepository.findByRunAndSample(runId, sampleId);

        Map<String, QCSiteStats> qcSiteStatsMap = convertPicardFilesToQcStats(picardFiles);

        return qcSiteStatsMap;
    }

    @GetMapping(value = "/picardstats/run-date/{dateInMilis}")
    public Map<String, QCSiteStats> getPicardStatsByDate(@PathVariable long dateInMilis) {
        Date date = new Date(dateInMilis);
        log.info(String.format("Retrieving picard stats for date %d (%s)", dateInMilis, date));

        List<PicardFile> picardFiles = picardFileRepository.findByLastModifiedGreaterThan(date);

        Map<String, QCSiteStats> qcSiteStatsMap = convertPicardFilesToQcStats(picardFiles);

        return qcSiteStatsMap;
    }

    /*
    Include files modified in the last n days.
     */
    public static class DaysFileFilter implements FileFilter {
        private static long MS_PER_DAY = 86400000L;
        int nDays;

        public DaysFileFilter(int nDays) {
            this.nDays = nDays;
        }

        @Override
        public boolean accept(File pathname) {
            long modifiedDeltaMS = System.currentTimeMillis() - pathname.lastModified();
            if (modifiedDeltaMS < (nDays * MS_PER_DAY))
                return true;
            else
                return false;
        }
    }

    /**
     * Reads all 380k+ Picard stats files written from 2014 to now
     * @throws Exception
     */
    public void buildDatabaseFromPicardFiles() throws Exception {
        System.out.println("Building entire database from all available Picard stats files.");
        for (String sequencer : ALL_SEQUENCERS) {
            File f = new File(BASE_STATS_DIR + sequencer);
            File [] statsFiles = f.listFiles();
            for (File statsFile: statsFiles) {
                saveStats(statsFile, false);
            }
        }

        System.exit(0);
    }

    protected void saveStats(File statsFile, boolean doNotOverwrite) throws FileNotFoundException, IllegalAccessException {
        String name = statsFile.getName();
        PicardFile pf = PicardFile.fromFile(statsFile);
        if (doNotOverwrite) {
            Optional<PicardFile> pfDb = picardFileRepository.findById(pf.getFilename());
            if (pfDb.isPresent()) {
                if (pfDb.get().getLastModified().getTime() == statsFile.lastModified()) {
                    System.out.println("DB already saved:" + pf.getFilename());
                    return;
                }
            }
        }

        System.out.println("Saving: " + name);
        if (pf == null) {
            pf = new PicardFile(name, null, null, null, null, null, null, false);
            picardFileRepository.save(pf);
            return;
        }

        // TODO interface and/or design pattern here
        if (name.endsWith("___AM.txt")) {
            List<AlignmentSummaryMetrics> amStats = AlignmentSummaryMetrics.readFile(statsFile, pf.getMd5RRS());
            if (amStats != null) {
                picardFileRepository.save(pf);
                amRepository.saveAll(amStats);
            } else {
                pf.setParseOK(false);
                picardFileRepository.save(pf);
                System.err.println("File is not in the correct format, ignoring:" + name);
            }
        } else if (name.endsWith("___oxoG.txt")) {
            CpcgMetrics dm = CpcgMetrics.readFile(statsFile, pf.getMd5RRS());
            if (dm != null) {
                picardFileRepository.save(pf);
                cpcgRepository.save(dm);
            } else {
                pf.setParseOK(false);
                picardFileRepository.save(pf);
                System.err.println("File is not in the correct format, ignoring:" + name);
            }
        } else if (name.endsWith("___MD.txt")) {
            DuplicationMetrics dm = DuplicationMetrics.readFile(statsFile, pf.getMd5RRS());
            if (dm != null) {
                picardFileRepository.save(pf);
                dmRepository.save(dm);
            } else {
                pf.setParseOK(false);
                picardFileRepository.save(pf);
                System.err.println("File is not in the correct format, ignoring:" + name);
            }
        } else if (name.endsWith("___HS.txt")) {
            HsMetrics x = HsMetrics.readFile(statsFile, pf.getMd5RRS());
            if (x != null) {
                picardFileRepository.save(pf);
                hsRepository.save(x);
            } else {
                pf.setParseOK(false);
                picardFileRepository.save(pf);
                System.err.println("File is not in the correct format, ignoring:" + name);
            }
        } else if (name.endsWith("___RNA.txt")) {
            RnaSeqMetrics x = RnaSeqMetrics.readFile(statsFile, pf.getMd5RRS());
            if (x != null) {
                picardFileRepository.save(pf);
                rnaSeqRepository.save(x);
            } else {
                pf.setParseOK(false);
                picardFileRepository.save(pf);
                System.err.println("File is not in the correct format, ignoring:" + name);
            }
        } else if (name.endsWith("___WGS.txt")) {
            WgsMetrics x = WgsMetrics.readFile(statsFile, pf.getMd5RRS());
            if (x != null) {
                picardFileRepository.save(pf);
                wgsRepository.save(x);
            } else {
                pf.setParseOK(false);
                picardFileRepository.save(pf);
                System.err.println("File is not in the correct format, ignoring:" + name);
            }
        } else if (name.endsWith("___mskQ.txt")) {
            QMetric x = QMetric.readFile(statsFile, pf.getMd5RRS());
            if (x != null) {
                picardFileRepository.save(pf);
                qMetricsRepository.save(x);
            } else {
                pf.setParseOK(false);
                picardFileRepository.save(pf);
                System.err.println("File is not in the correct format, ignoring:" + name);
            }
        }
    }
}