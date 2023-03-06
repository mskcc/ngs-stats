package org.mskcc.picardstats.model;

import lombok.ToString;

import javax.persistence.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Scanner;

/*
https://github.com/broadinstitute/picard/blob/master/src/main/java/picard/analysis/RnaSeqMetrics.java
 */
@Entity
@Table(name = "rnaseqmetrics")
@ToString
public class RnaSeqMetrics {
    @Id
    @Column(length = 150)
    public String filename;
    @Column(length = 32)
    public String md5RRS;

    @OneToOne
    @JoinColumn(name = "filename")
    private PicardFile picardFile;

    public long PF_BASES;
    public long PF_ALIGNED_BASES;
    @Column(nullable = true)
    public Long RIBOSOMAL_BASES;
    public long CODING_BASES;
    public long UTR_BASES;
    public long INTRONIC_BASES;
    public long INTERGENIC_BASES;
    public long IGNORED_READS;
    public long CORRECT_STRAND_READS;
    public long INCORRECT_STRAND_READS;
    public long NUM_R1_TRANSCRIPT_STRAND_READS;
    public long NUM_R2_TRANSCRIPT_STRAND_READS;
    public long NUM_UNEXPLAINED_READS;
    public double PCT_R1_TRANSCRIPT_STRAND_READS;
    public double PCT_R2_TRANSCRIPT_STRAND_READS;
    @Column(nullable = true)
    public Double PCT_RIBOSOMAL_BASES;
    public double PCT_CODING_BASES;
    public double PCT_UTR_BASES;
    public double PCT_INTRONIC_BASES;
    public double PCT_INTERGENIC_BASES;
    public double PCT_MRNA_BASES;
    public double PCT_USABLE_BASES;
    public double PCT_CORRECT_STRAND_READS;
    public double MEDIAN_CV_COVERAGE;
    public double MEDIAN_5PRIME_BIAS;
    public double MEDIAN_3PRIME_BIAS;
    public double MEDIAN_5PRIME_TO_3PRIME_BIAS;


    public static int headerRow = 6;
    public static RnaSeqMetrics readFile(File file, String md5RRS) throws FileNotFoundException, IllegalAccessException {
        Scanner scan = new Scanner(file);
        for (int i=0;i < headerRow;i++) {
            if (scan.hasNext())
                scan.nextLine();
            else {
                return null;
            }
        }

        String [] columnHeaders = scan.nextLine().split("\t");

        if (scan.hasNext()) {
            String [] parts = scan.nextLine().split("\t");
            RnaSeqMetrics x = new RnaSeqMetrics();
            x.filename = file.getName();
            x.md5RRS = md5RRS;
            Field[] fields = x.getClass().getFields();

            HashMap<String, Field> nameToField = new HashMap<>();
            for (Field f : fields)
                nameToField.put(f.getName(), f);

            for (int i = 0; i < parts.length; i++) {
                String value = parts[i];
                if ("".equals(value)) // some columns are nullable
                    continue;
                if ("?".equals(value))
                    return null;

                String fieldName = columnHeaders[i];
                Field field = nameToField.get(fieldName);
                if (field.getType().getName().equals("double"))
                    field.setDouble(x, Double.parseDouble(value));
                else if (field.getType().getName().equals("java.lang.Double"))
                    field.set(x, Double.valueOf(value));
                else if (field.getType().getName().equals("long"))
                    field.setLong(x, Long.parseLong(value));
                else
                    field.set(x, Long.valueOf(value));
            }
            return x;
        }
        return null;
    }
}