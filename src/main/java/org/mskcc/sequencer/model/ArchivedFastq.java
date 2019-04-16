package org.mskcc.sequencer.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter @Setter
@ToString
@AllArgsConstructor
@Entity
/**
 * A fastq.gz file that has been archived.
 * The absolute path and some meta-data.
 */
public class ArchivedFastq {
    private static Logger log = LoggerFactory.getLogger(ArchivedFastq.class);

    private String run;
    private String runBaseDirectory;
    private String project;
    private String sample;

    @Id
    @Column(length = 500)
    private String fastq;

    private Date fastqLastModified;

    private Date lastUpdated;

    public static List<ArchivedFastq> walkDirectory(String baseDirectory, String directory) throws IOException  {
        Path rootPath = new File(baseDirectory + directory).toPath();
        log.info("Walking directory to find fastq.gz files: " + rootPath);

        String runBaseDirectory = rootPath.getFileName().toString();
        String [] runParts = runBaseDirectory.split("_");
        String run;
        if (runParts.length > 3)
            run = runParts[0] + "_" + runParts[1] + "_" + runParts[2];
        else
            run = runBaseDirectory;

        List<ArchivedFastq> fastqs = new ArrayList<>();
        Files.walk(rootPath, 4).filter(p -> p.toString().endsWith(".fastq.gz")).forEach(y->save(run, runBaseDirectory, y, fastqs));
        log.info("Total fastq.gz files found: " + fastqs.size());
        return fastqs;
    }

    protected static void save(String run, String runBaseDirectory,  Path p, List<ArchivedFastq> paths) {
        String filename = p.getFileName().toString();
        if (filename.contains("Undetermined_")) {
            ArchivedFastq f = new ArchivedFastq(run, runBaseDirectory, null, null, p.toString(), new Date(p.toFile().lastModified()), new Date());
            paths.add(f);
        } else {
            Path samplePath = p.getParent();
            String sample = samplePath.getFileName().toString().substring(7);    // remove "Sample_" prefix
            Path projectPath = samplePath.getParent();
            String project = projectPath.getFileName().toString().substring(8);  // remove "Project_" prefix

            ArchivedFastq f = new ArchivedFastq(run, runBaseDirectory, project, sample, p.toAbsolutePath().toString(), new Date(p.toFile().lastModified()), new Date());
            paths.add(f);
        }
    }
}