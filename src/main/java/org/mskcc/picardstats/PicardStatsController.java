package org.mskcc.picardstats;

import org.mskcc.picardstats.model.*;
import org.mskcc.picardstats.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.util.*;

@RestController
public class PicardStatsController {

    private static final String BASE_STATS_DIR = "/ifs/data/BIC/Stats/hiseq/DONE/"; // "/Users/mcmanamd/Downloads/DONE/";

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
        if (files == null || files.size() == 0)
            return null;

        HashMap<String, QCSiteStats> statsMap = new HashMap<>(); // Run-Request-Sample to QCSiteStats
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
                default:
                    System.out.println("File type ignored by QC site:" + pf.getFileType());
            }
            statsMap.put(pf.getMd5RRS(), stats);
        }

        System.out.println("Elapsed time pooled normal stats queries (ms): " + (System.currentTimeMillis() - startTime));
        List<QCSiteStats> result = new ArrayList(statsMap.values());
        return result;
    }

    @RequestMapping(value = "/picardstats/update/{days}", method = RequestMethod.GET)
    public String updateDatabase(@PathVariable Integer days) throws Exception {
        System.out.println("Updating the database for files written in the past " + days + " days.");

        File baseDir = new File(BASE_STATS_DIR);
        if (!baseDir.exists()) {
            String msg = "Base directory does not exist: " + BASE_STATS_DIR;
            System.err.println(msg);
            return "ERROR: " + msg;
        }

        DaysFileFilter dff = new DaysFileFilter(days);
        for (String sequencer : ACTIVE_SEQUENCERS) {
            File f = new File(BASE_STATS_DIR + sequencer);
            File [] statsFiles = f.listFiles(dff);
            System.out.println("Processing sequencer: " + sequencer + " files to process: " + statsFiles.length);
            for (File statsFile: statsFiles) {
                saveStats(statsFile, true);
            }
        }
        return "DONE";
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
    protected void buildDatabaseFromPicardFiles() throws Exception {
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