package org.mskcc.picardstats;

import org.mskcc.picardstats.model.*;
import org.mskcc.picardstats.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class PicardStatsController {

    private static final String BASE_STATS_DIR = "/Users/mcmanamd/Downloads/DONE/";

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


    @RequestMapping(value = "/picardstats/request/{requestid}", method = RequestMethod.GET)
    public String getQCSiteStats(@PathVariable String requestId) {
        return "needNewClass";
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

    @RequestMapping(value = "/picardstats/update/{days}", method = RequestMethod.GET)
    public String updateDatabase(@PathVariable Integer days) throws Exception {
        System.out.println("Updating the database for files written in the past " + days + " days.");

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