package org.mskcc.picardstats.model;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.codec.digest.DigestUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.io.*;
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
    private String picardVersion;

    @OneToOne(mappedBy = "picardFile")
    private AlignmentSummaryMetrics alignmentSummaryMetrics;

    @OneToOne(mappedBy = "picardFile")
    private CpcgMetrics cpcgMetrics;

    @OneToOne(mappedBy = "picardFile")
    private DuplicationMetrics duplicationMetrics;

    @OneToOne(mappedBy = "picardFile")
    private HsMetrics hsMetrics;

    @OneToOne(mappedBy = "picardFile")
    private QMetric qMetric;

    @OneToOne(mappedBy = "picardFile")
    private RnaSeqMetrics rnaSeqMetrics;

    @OneToOne(mappedBy = "picardFile")
    private WgsMetrics wgsMetrics;

    public PicardFile() {}

    public PicardFile(String filename, String run, String request, String sample, String referenceGenome,
                      String fileType, Date lastModified, boolean parseOK, String picardVersion) {
        this.filename = filename;
        this.run = run;
        this.request = request;
        this.sample = sample;
        this.referenceGenome = referenceGenome;
        this.fileType = fileType;
        this.lastModified = lastModified;
        this.parseOK = parseOK;
        this.picardVersion = picardVersion;

        if (run != null && request != null && sample != null) {
            this.md5RRS = DigestUtils.md5Hex(this.run + this.request + this.sample);
        }
    }

    /**
     * Parses a file name like:
     * 'DIANA_0008_AH3V2JDMXX___P07951_I___P-0005083-N01-WES_IGO_07951_I_11___hg19___MD.txt'
     * or
     * DIANA_0008_AH3V2JDMXX___P07951_I___P-0005083-N01-WES_IGO_07951_I_11___hg19___2_21_2___MD.txt'
     * @param file
     */
    public static PicardFile fromFile(File file) {
        String filename = file.getName();
        String [] parts = filename.split("___");

        if (parts.length != 5 && parts.length != 6) {
            System.err.println("File name format unknown.");
            return null;
        }

        String run = parts[0];
        String request = parts[1].substring(1); // remove P
        String sample = parts[2];
        String referenceGenome = parts[3];

        String version = null;
        String fileType;
        // read version from file name like
        // DIANA_0508_BHVV72DSX3___P13340_B___TM_20220614_Exp1_T65_beta_IGO_13340_B_65___grcm39___2_23_2___AM.txt
        if (parts.length == 6) {
            version = parts[4];
            fileType = parts[5].substring(0, parts[5].length()-4); // remove .txt
        } else {
            fileType = parts[4].substring(0, parts[4].length()-4); // remove .txt
            try {
                System.out.println("Reading DRAGEN version number");
                BufferedReader br = new BufferedReader(new FileReader(file));
                String firstLine = br.readLine();
                // DRAGEN .txt files are written with the first line like:
                // '#DRAGEN_VERSION_01.003.044.3.10.1-183-g9ced7ae8 '
                if (firstLine != null && firstLine.contains("#DRAGEN_VERSION")) {
                    version = getVersionDRAGEN(firstLine);
                }
                br.close();
            } catch (IOException e) {
                System.err.println("Failed to read DRAGEN version number." + e.getMessage());
            }
        }

        return new PicardFile(filename, run, request, sample, referenceGenome, fileType, new Date(file.lastModified()), true, version);
    }

    protected static String getVersionDRAGEN(String firstLine) {
        firstLine = firstLine.replace("#DRAGEN_VERSION_", "");
        return "DRAGEN_" + firstLine.split(" ")[0];
    }
}