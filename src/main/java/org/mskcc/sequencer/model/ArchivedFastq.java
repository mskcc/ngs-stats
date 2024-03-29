package org.mskcc.sequencer.model;

import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "archivedfastq")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
/**
 * A fastq.gz file that has been archived.
 * The absolute path and some meta-data.
 */
public class ArchivedFastq {
    private static Logger log = LoggerFactory.getLogger(ArchivedFastq.class);

    private String run;
    private String runBaseDirectory;
    private String project;
    private String sample; // from Jan. 2016 format example: A123456_V3_IGO_07973_8 ie sampleId_IGO_igoId

    @Id
    @Column(length = 250) // Cause of "Specified key was too long; max key length is 1000 bytes" error? Fix: <=250
    private String fastq;

    private Date fastqLastModified;

    private Long bytes;

    private Date lastUpdated;

    public static List<String> toFastqPathOnly(List<ArchivedFastq> x) {
        List<String> list = x.stream().map(ArchivedFastq::getFastq).collect(Collectors.toList());
        return list;
    }

    public String getSampleName() {
        if (sample.contains("_IGO_"))
            return sample.substring(0, sample.indexOf("_IGO_"));
        else
            return sample;
    }

    /**
     * Walks a FASTQ run runDirectory looking for fastq.gz or fastq.ora files.
     *
     * @param baseDirectory directory where FASTQ runs are written
     * @param runDirectory run directory
     * @return
     * @throws IOException
     */
    public static List<ArchivedFastq> walkDirectory(String baseDirectory, String runDirectory) throws IOException  {
        if (!baseDirectory.endsWith("/"))
            baseDirectory = baseDirectory + "/";

        Path rootPath = new File(baseDirectory + runDirectory).toPath();
        log.info("Walking runDirectory to find .fastq.gz, .fastq.or & .fastq files: " + rootPath);

        String runBaseDirectory = rootPath.getFileName().toString();
        String [] runParts = runBaseDirectory.split("_");
        String run;
        if (runParts.length > 3)
            run = runParts[0] + "_" + runParts[1] + "_" + runParts[2];
        else
            run = runBaseDirectory;

        List<ArchivedFastq> fastqs = new ArrayList<>();
        Files.walk(rootPath, 4).filter(p -> p.toString().endsWith(".fastq.gz")).forEach(y->save(run, runBaseDirectory, y, fastqs));
        log.info("Re-walking adding fastq.ora fastqs"); // slow re-walk but the directories are walked rarely
        Files.walk(rootPath, 4).filter(p -> p.toString().endsWith(".fastq.ora")).forEach(y->save(run, runBaseDirectory, y, fastqs));
        log.info("Total .fastq.gz & .fastq.ora files found: " + fastqs.size());
        if (fastqs.size() == 0) {
            // SCOTT_0296_AHKL2MBGXH_A1 is a custom demux with uncompressed data such as
            // 501_shNIPBL5_6D_1_CAGEtag_IGO_11724_3_L001_R1.fastq
            Files.walk(rootPath, 4).filter(p -> p.toString().endsWith(".fastq")).forEach(y->save(run, runBaseDirectory, y, fastqs));
            log.info("Total .fastq files found: " + fastqs.size());
        }
        return fastqs;
    }

    protected static void save(String run, String runBaseDirectory, Path p, List<ArchivedFastq> paths) {
        String filename = p.getFileName().toString();
        Long bytes = p.toFile().length();
        if (filename.contains("Undetermined_")) {
            ArchivedFastq f = new ArchivedFastq(run, runBaseDirectory, null, null, p.toString(), new Date(p.toFile().lastModified()), bytes, new Date());
            paths.add(f);
        } else {
            // DLP project fastq.gz files do not start with "Sample_" all others do
            // DLP projects do not have a separate folder for each sample
            String sample;
            Path projectPath;
            Path samplePath = p.getParent();
            if (samplePath.getFileName().toString().startsWith("Sample_")) {
                sample = samplePath.getFileName().toString().substring(7);    // remove "Sample_" prefix
                projectPath = samplePath.getParent();
            } else {
                log.info("Processing fastq.gz files in the same directory");
                // 065RA_DLP_UNSORTED_128655A_23_54_IGO_11113_C_2_1_671_S671_L003_R1_001.fastq.gz
                // remove _S*_L*_001.fastq.gz
                sample = getSampleName(filename);
                projectPath = p.getParent();
            }
            String project = projectPath.getFileName().toString().substring(8);  // remove "Project_" prefix

            ArchivedFastq f = new ArchivedFastq(run, runBaseDirectory, project, sample, p.toAbsolutePath().toString(), new Date(p.toFile().lastModified()), bytes, new Date());
            paths.add(f);
        }
    }

    protected static String getSampleName(String filename) {
        // Normal IGO fastq naming:
        // "065RA_DLP_UNSORTED_128655A_23_51_IGO_11113_C_2_1_631_S631_L003_R1_001.fastq.gz" or ending with fastq.ora
        // "065RA_DLP_UNSORTED_128655A_23_51_IGO_11113_C_2_1_631_S631_L003_R1_001.fastq.ora
        // however some very old 2012 fastqs do not have "_S" and sample name won't work - for example:
        // /igo/delivery/FASTQ/LIZ_0206_AD186DACXX/Project_3075/Sample_EB-LKO-Ac-II/EB-LKO-Ac-II_AGTCAA_L002_R1_002.fastq.ora
        return filename.replaceFirst("_S[0-9]+_L[0-9]+_R[0-9]_001.fastq.(gz|ora)", "");
    }
}