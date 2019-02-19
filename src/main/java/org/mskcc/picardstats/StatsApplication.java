package org.mskcc.picardstats;

import org.mskcc.picardstats.model.*;
import org.mskcc.picardstats.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

@SpringBootApplication
public class StatsApplication implements CommandLineRunner {

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


    public static void main(String[] args) {
        SpringApplication.run(StatsApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        if (args.length > 0 && args[0].equals("-updateDatabase"))
            readRecentPicardFiles();

        if (args.length > 0 && args[0].equals("-buildDatabase"))
            buildDatabaseFromPicardFiles();
    }

    /**
     *  Read any Picard files not in the database.
     */
    protected void readRecentPicardFiles() {
        // TODO
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
                saveStats(statsFile);
            }
        }

        System.exit(0);
    }

    protected void saveStats(File statsFile) throws FileNotFoundException, IllegalAccessException {
        String name = statsFile.getName();
        System.out.println("Saving: " + name);
        PicardFile pf = PicardFile.fromFile(statsFile);
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