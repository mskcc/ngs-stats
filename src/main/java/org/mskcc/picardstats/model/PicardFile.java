package org.mskcc.picardstats.model;

import org.apache.commons.codec.digest.DigestUtils;

import javax.persistence.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Date;
import java.util.List;

@Entity
public class PicardFile {
    @Id
    @Column(length = 150)
    private String filename;

    private String run;

    private String request;

    private String sample;

    private String referenceGenome;

    private String fileType;

    private Date fileCreated;

    private Date fileImported = new Date();

    @Column(length = 32)
    private String md5RRS;

    private boolean parseOK = true; // if the format is bad or unrecognized and parsing fails set to false

    @OneToMany
    @JoinColumn(name="filename")
    private List<AlignmentSummaryMetrics> alignmentMetrics;


    public PicardFile() {}

    public PicardFile(String filename, String run, String request, String sample, String referenceGenome,
                      String fileType, Date fileCreated, boolean parseOK) {
        this.filename = filename;
        this.run = run;
        this.request = request;
        this.sample = sample;
        this.referenceGenome = referenceGenome;
        this.fileType = fileType;
        this.fileCreated = fileCreated;
        this.parseOK = parseOK;

        if (run != null && request != null && sample != null) {
            this.md5RRS = DigestUtils.md5Hex(this.run + this.request + this.sample);
        }
    }

    /**
     * Parses a file name like:
     * 'DIANA_0008_AH3V2JDMXX___P07951_I___P-0005083-N01-WES_IGO_07951_I_11___hg19___MD.txt'
     * @param file
     */
    public static PicardFile fromFile(File file) {
        String filename = file.getName();
        String [] parts = filename.split("___");

        if (parts.length != 5) {
            System.err.println("File name format unknown.");
            return null;
        }

        String run = parts[0];
        String request = parts[1].substring(1); // remove P
        String sample = parts[2];
        String referenceGenome = parts[3];
        String fileType = parts[4].substring(0, parts[4].length()-4); // remove .txt

        BasicFileAttributes attrs;
        Date fileCreated = null;
        try {
            attrs = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
            FileTime time = attrs.creationTime();

            fileCreated = new Date(time.toMillis());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new PicardFile(filename, run, request, sample, referenceGenome, fileType, fileCreated, true);
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getRun() {
        return run;
    }

    public void setRun(String run) {
        this.run = run;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getSample() {
        return sample;
    }

    public void setSample(String sample) {
        this.sample = sample;
    }

    public String getReferenceGenome() {
        return referenceGenome;
    }

    public void setReferenceGenome(String referenceGenome) {
        this.referenceGenome = referenceGenome;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Date getFileCreated() {
        return fileCreated;
    }

    public void setFileCreated(Date fileCreated) {
        this.fileCreated = fileCreated;
    }

    public Date getFileImported() {
        return fileImported;
    }

    public void setFileImported(Date fileImported) {
        this.fileImported = fileImported;
    }

    public List<AlignmentSummaryMetrics> getAlignmentMetrics() {
        return alignmentMetrics;
    }

    public void setAlignmentMetrics(List<AlignmentSummaryMetrics> alignmentMetrics) {
        this.alignmentMetrics = alignmentMetrics;
    }

    public boolean isParseOK() {
        return parseOK;
    }

    public void setParseOK(boolean parseOK) {
        this.parseOK = parseOK;
    }

    public String getMd5RRS() {
        return md5RRS;
    }

    public void setMd5RRS(String md5RRS) {
        this.md5RRS = md5RRS;
    }
}