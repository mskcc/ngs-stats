package org.mskcc.picardstats;

import org.mskcc.picardstats.model.*;
import org.mskcc.picardstats.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class PicardStatsController {
    private static final String [] BASE_STATS_DIR =  new String [] {"/igo/stats/DONE/"};

    // directories where Picard stats Excel files are written
    final String RUN_REPORTS_SHARED_DIR = "/data/picardExcel/run_reports/";
    final String PROJ_REPORTS_SHARED_DIR = "/data/picardExcel/project_reports/";

    private static final String [] ACTIVE_SEQUENCERS =
            {"DIANA", "RUTH", "JOHNSAWYERS", "FAUCI", "AMELIE", "PEPE", "SCOTT"};

    private static final String [] ALL_SEQUENCERS =
            {"BRAD", "DIANA", "JAX", "JOHNSAWYERS", "KIM", "LIZ", "LOLA", "MICHELLE", "MOMO", "PITT", "SCOTT", "TOMS", "VIC", "RUTH", "AMELIE", "PEPE", "FAUCI"};

    @Autowired
    private WgsMetricsRepository wgsRepository;
    @Autowired
    private CpcgMetricsRepository cpcgRepository;
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

    @CrossOrigin
    @GetMapping(value = "/get-picard-run-excel/{run}")
    public @ResponseBody byte[] getRunExcelFile(@PathVariable String run, HttpServletResponse response)
            throws IOException {
        String filename = "AutoReport_" + run + ".xls";
        return readExcelFile(RUN_REPORTS_SHARED_DIR, filename, response);
    }

    @CrossOrigin
    @GetMapping(value = "/get-picard-project-excel/{project}")
    public @ResponseBody byte[] getProjectExcelFile(@PathVariable String project, HttpServletResponse response)
            throws IOException {
        String filename = "AutoReport_P" + project + ".xls";
        return readExcelFile(PROJ_REPORTS_SHARED_DIR, filename, response);
    }

    protected static byte[] readExcelFile(String baseDir, String filename, HttpServletResponse response)
            throws IOException {
        response.addHeader("Content-disposition", "attachment;filename=" + filename);
        response.setContentType("xls");

        System.out.println("Reading Excel file: " + filename);
        Path path = Paths.get(baseDir + filename);
        byte[] bArray = Files.readAllBytes(path);
        return bArray;
    }

    @RequestMapping(value = "/picardtoexcel", method = RequestMethod.GET)
    // generate the Excel files on demand or via crontab? (can be a bit slow so use crontab?)
    public String writeRecentStatsToExcel() {
        // store to the shared location if the directories exist on the computer where we are running
        String runBasePath = "";
        if (new File(RUN_REPORTS_SHARED_DIR).exists()) {
            runBasePath = RUN_REPORTS_SHARED_DIR;
        }
        String projBasePath = "";
        if (new File(PROJ_REPORTS_SHARED_DIR).exists()) {
            projBasePath = PROJ_REPORTS_SHARED_DIR;
        }
        System.out.println("Writing Picard Excel files.");

        // historically the excel files have the date appended to them
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());

        List<String> recentRequests = picardFileRepository.findRecentRequests();
        for (String request : recentRequests) {
            File projectFileName = new File(projBasePath + "AutoReport_P" + request + ".xls");
            System.out.println("Writing: " + projectFileName.getAbsolutePath());
            try {
                PicardToExcel.writeExcel(projectFileName, picardFileRepository.findByRequest(request));
                projectFileName.setReadable(true, false);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Failed to write: " + projectFileName);
            }
        }

        List<String> recentRuns = picardFileRepository.findRecentRuns();
        for (String run : recentRuns) {
            File runFileName = new File(runBasePath + "AutoReport_" + run + ".xls");
            System.out.println("Writing: " + runFileName.getAbsolutePath());
            try {
                PicardToExcel.writeExcel(runFileName, picardFileRepository.findByRun(run));
                runFileName.setReadable(true, false);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Failed to write: " + runFileName);
            }
        }

        System.out.println("Picard to Excel complete.");
        return "Request and Run Excel files Generated.";
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
            System.out.println("Reading stats file: " + pf.getFilename());
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
        long startTime = System.currentTimeMillis(); // the NY Isilon RAID is slow so track elapsed time

        // for each active sequencer, list new files created within last n days
        for (String baseStatsDir : BASE_STATS_DIR ) {
            for (String sequencer : ACTIVE_SEQUENCERS) {
                File f = new File(baseStatsDir + sequencer);
                if (!f.exists())
                    continue;
                File[] statsFiles = f.listFiles(new DaysFileFilter(nDays));
                long elapsedTime = System.currentTimeMillis() - startTime;
                System.out.println("Processing sequencer: " + sequencer + " files to process: " + statsFiles.length + ", Elapsed time (ms): " + elapsedTime);
                for (File statsFile : statsFiles) {
                    saveStats(statsFile, true);
                }
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

        int filesParsed = 0;
        for (String baseStatsDir : BASE_STATS_DIR) {
            File statsDir = new File(baseStatsDir + sequencer);
            System.out.println("Looking for stats files in directory: " + statsDir);

            FilenameFilter prefixFilter = (dir, name) -> {
                if (name.startsWith(run)) {
                    return true;
                } else {
                    return false;
                }
            };
            File[] statsFiles = statsDir.listFiles(prefixFilter);
            if (statsFiles == null || statsFiles.length == 0) {
                continue;
            } else {
                filesParsed += statsFiles.length;
            }

            for (File statsFile : statsFiles) {
                saveStats(statsFile, true);
            }
        }
        return "Files parsed: " + filesParsed;
    }

    @GetMapping(value = "/picardstats/updaterunWGS")
    public String updateDatabaseByRunWGS() throws Exception {

        int filesParsed = 0;
        for (String baseStatsDir : BASE_STATS_DIR) {
            File statsDir = new File("/igo/stats/DONE/MEDIAN");
            System.out.println("Looking for stats files in directory: " + statsDir);

            FilenameFilter prefixFilter = (dir, name) -> {
                if (name.endsWith("WGS.txt")) {
                    return true;
                } else {
                    return false;
                }
            };
            File[] statsFiles = statsDir.listFiles(prefixFilter);
            if (statsFiles == null || statsFiles.length == 0) {
                continue;
            } else {
                filesParsed += statsFiles.length;
            }

            for (File statsFile : statsFiles) {
                saveStats(statsFile, true);
            }
        }
        return "Files parsed: " + filesParsed;
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
        System.out.println(String.format("Retrieving picard stats for date %d (%s)", dateInMilis, date));

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
            File f = new File(BASE_STATS_DIR[0] + sequencer);
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
            pf = new PicardFile(name, null, null, null, null, null, null, false, null);
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
        }
    }
}