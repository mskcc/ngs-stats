package org.mskcc.picardstats.model;

import javax.persistence.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Scanner;

@Entity
/*
https://github.com/broadinstitute/picard/blob/master/src/main/java/picard/analysis/CollectWgsMetrics.java
 */
public class WgsMetrics {
    @Id
    @Column(length = 150)
    public String filename;
    @Column(length = 32)
    public String md5RRS;


    /** The number of non-N bases in the genome reference over which coverage will be evaluated. */
    /** The number of non-N bases in the genome reference over which coverage will be evaluated. */
    public long GENOME_TERRITORY;
    /** The mean coverage in bases of the genome territory, after all filters are applied. */
    
    public double MEAN_COVERAGE;
    /** The standard deviation of coverage of the genome after all filters are applied. */
    
    public double SD_COVERAGE;
    /** The median coverage in bases of the genome territory, after all filters are applied. */
    
    public double MEDIAN_COVERAGE;
    /** The median absolute deviation of coverage of the genome after all filters are applied. */
    
    public double MAD_COVERAGE;

    /** The fraction of aligned bases that were filtered out because they were in reads with low mapping quality (default is < 20). */
    
    public double PCT_EXC_MAPQ;
    /** The fraction of aligned bases that were filtered out because they were in reads marked as duplicates. */
    
    public double PCT_EXC_DUPE;
    /** The fraction of aligned bases that were filtered out because they were in reads without a mapped mate pair. */
    
    public double PCT_EXC_UNPAIRED;
    /** The fraction of aligned bases that were filtered out because they were of low base quality (default is < 20). */
    
    public double PCT_EXC_BASEQ;
    /** The fraction of aligned bases that were filtered out because they were the second observation from an insert with overlapping reads. */
    
    public double PCT_EXC_OVERLAP;
    /** The fraction of aligned bases that were filtered out because they would have raised coverage above the capped value (default cap = 250x). */
    
    public double PCT_EXC_CAPPED;
    /** The total fraction of aligned bases excluded due to all filters. */
    
    public double PCT_EXC_TOTAL;

    /** The fraction of bases that attained at least 1X sequence coverage in post-filtering bases. */
    
    public double PCT_1X;
    /** The fraction of bases that attained at least 5X sequence coverage in post-filtering bases. */
    
    public double PCT_5X;
    /** The fraction of bases that attained at least 10X sequence coverage in post-filtering bases. */
    
    public double PCT_10X;
    /** The fraction of bases that attained at least 15X sequence coverage in post-filtering bases. */
    
    public double PCT_15X;
    /** The fraction of bases that attained at least 20X sequence coverage in post-filtering bases. */
    
    public double PCT_20X;
    /** The fraction of bases that attained at least 25X sequence coverage in post-filtering bases. */
    
    public double PCT_25X;
    /** The fraction of bases that attained at least 30X sequence coverage in post-filtering bases. */
    
    public double PCT_30X;
    /** The fraction of bases that attained at least 40X sequence coverage in post-filtering bases. */
    
    public double PCT_40X;
    /** The fraction of bases that attained at least 50X sequence coverage in post-filtering bases. */
    
    public double PCT_50X;
    /** The fraction of bases that attained at least 60X sequence coverage in post-filtering bases. */
    
    public double PCT_60X;
    /** The fraction of bases that attained at least 70X sequence coverage in post-filtering bases. */
    
    public double PCT_70X;
    /** The fraction of bases that attained at least 80X sequence coverage in post-filtering bases. */
    
    public double PCT_80X;
    /** The fraction of bases that attained at least 90X sequence coverage in post-filtering bases. */
    
    public double PCT_90X;
    /** The fraction of bases that attained at least 100X sequence coverage in post-filtering bases. */
    
    public double PCT_100X;

    /** The theoretical HET SNP sensitivity. */
    
    public double HET_SNP_SENSITIVITY;

    /** The Phred Scaled Q Score of the theoretical HET SNP sensitivity. */
    
    public double HET_SNP_Q;

    public static int headerRow = 6;
    public static WgsMetrics readFile(File file, String md5RRS) throws FileNotFoundException, IllegalAccessException {
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
            WgsMetrics x = new WgsMetrics();
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
                else if ("?".equals(value))
                    return null;

                String fieldName = columnHeaders[i];
                Field field = nameToField.get(fieldName);
                if (field.getType().getName().equals("double"))
                    field.setDouble(x, Double.parseDouble(parts[i]));
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