package org.mskcc.picardstats.model;

import lombok.ToString;

import javax.persistence.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Scanner;

//https://github.com/broadinstitute/picard/blob/master/src/main/java/picard/analysis/directed/HsMetrics.java
@Entity
@ToString
public class HsMetrics {
    @Id
    @Column(length = 150)
    public String filename;
    @Column(length = 32)
    public String md5RRS;

    @OneToOne
    @JoinColumn(name = "filename")
    private PicardFile picardFile;

    public String BAIT_SET;

    public long GENOME_SIZE;
    public long BAIT_TERRITORY;
    public long TARGET_TERRITORY;
    public double BAIT_DESIGN_EFFICIENCY;
    public long TOTAL_READS;
    public long PF_READS;
    public long PF_UNIQUE_READS;
    public double PCT_PF_READS;
    public double PCT_PF_UQ_READS;
    public long PF_UQ_READS_ALIGNED;
    public double PCT_PF_UQ_READS_ALIGNED;
    public long PF_BASES_ALIGNED;
    public long PF_UQ_BASES_ALIGNED;
    public long ON_BAIT_BASES;
    public long NEAR_BAIT_BASES;
    public long OFF_BAIT_BASES;
    public long ON_TARGET_BASES;
    public double PCT_SELECTED_BASES;
    public double PCT_OFF_BAIT;
    public double ON_BAIT_VS_SELECTED;
    public double MEAN_BAIT_COVERAGE;
    public double MEAN_TARGET_COVERAGE;
    public double MEDIAN_TARGET_COVERAGE;
    public long MAX_TARGET_COVERAGE;
    public double PCT_USABLE_BASES_ON_BAIT;
    public double PCT_USABLE_BASES_ON_TARGET;
    public double FOLD_ENRICHMENT;
    public double ZERO_CVG_TARGETS_PCT;
    public double PCT_EXC_DUPE;
    public double PCT_EXC_MAPQ;
    public double PCT_EXC_BASEQ;
    public double PCT_EXC_OVERLAP;
    public double PCT_EXC_OFF_TARGET;
    public double FOLD_80_BASE_PENALTY;
    public double PCT_TARGET_BASES_1X;
    public double PCT_TARGET_BASES_2X;
    public double PCT_TARGET_BASES_10X;
    public double PCT_TARGET_BASES_20X;
    public double PCT_TARGET_BASES_30X;
    public double PCT_TARGET_BASES_40X;
    public double PCT_TARGET_BASES_50X;
    public double PCT_TARGET_BASES_100X;
    @Column(nullable = true)
    public Long HS_LIBRARY_SIZE;
    public double HS_PENALTY_10X;
    public double HS_PENALTY_20X;
    public double HS_PENALTY_30X;
    public double HS_PENALTY_40X;
    public double HS_PENALTY_50X;
    public double HS_PENALTY_100X;
    public double AT_DROPOUT;
    public double GC_DROPOUT;
    public double HET_SNP_SENSITIVITY;
    public double HET_SNP_Q;

    public static int headerRow = 6;
    public static HsMetrics readFile(File file, String md5) throws FileNotFoundException, IllegalAccessException {
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
            HsMetrics x = new HsMetrics();
            x.filename = file.getName();
            x.md5RRS = md5;
            Field[] fields = x.getClass().getFields();

            HashMap<String, Field> nameToField = new HashMap<>();
            for (Field f : fields)
                nameToField.put(f.getName(), f);

            for (int i = 0; i < parts.length; i++) {
                String fieldName = columnHeaders[i];
                Field field = nameToField.get(fieldName);
                String typeName = field.getType().getName();

                String value = parts[i];
                if ("".equals(value)) // some columns are nullable
                    continue;
                else if ("?".equals(value)) {
                    System.err.println("Failing due to type:" + typeName + " fieldName:" + fieldName);
                    return null;
                }

                if (field.getType().getName().equals("double"))
                    field.setDouble(x, Double.parseDouble(value));
                else if (field.getType().getName().equals("java.lang.Double"))
                    field.set(x, Double.valueOf(value));
                else if (field.getType().getName().equals("long"))
                    field.setLong(x, Long.parseLong(value));
                else if (field.getType().getName().equals("java.lang.String"))
                    field.set(x, value);
                else
                    field.set(x, Long.valueOf(value));
            }
            return x;
        }
        return null;
    }
}