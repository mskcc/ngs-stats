package org.mskcc.picardstats.model;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.codec.digest.DigestUtils;

import javax.persistence.*;
import java.io.File;
import java.util.Date;

@Entity
@Getter @Setter
public class PicardFile {
    @Id
    @Column(length = 150)
    private String filename;

    private String run;
    private String request;
    private String sample;

    private String referenceGenome;
    private String fileType;
    private Date lastModified;
    private Date fileImported = new Date();
    @Column(length = 32)
    private String md5RRS;
    private boolean parseOK = true; // if the format is bad or unrecognized and parsing fails set to false

    public PicardFile() {}

    public PicardFile(String filename, String run, String request, String sample, String referenceGenome,
                      String fileType, Date lastModified, boolean parseOK) {
        this.filename = filename;
        this.run = run;
        this.request = request;
        this.sample = sample;
        this.referenceGenome = referenceGenome;
        this.fileType = fileType;
        this.lastModified = lastModified;
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

        return new PicardFile(filename, run, request, sample, referenceGenome, fileType, new Date(file.lastModified()), true);
    }
}