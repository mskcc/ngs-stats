package org.mskcc.picardstats.model;

import lombok.ToString;

import javax.persistence.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Scanner;

/*
This file has ten columns and a histogram.
https://github.com/broadinstitute/picard/blob/master/src/main/java/picard/sam/DuplicationMetrics.java
 */
@Entity
@Table(name = "duplicationmetrics")
@ToString
public class DuplicationMetrics {
    @Id
    @Column(length = 150)
    public String filename;
    @Column(length = 32)
    public String md5RRS;

    @OneToOne
    @JoinColumn(name = "filename")
    private PicardFile picardFile;

    public String LIBRARY;
    public long UNPAIRED_READS_EXAMINED;
    public long READ_PAIRS_EXAMINED;
    @Column(nullable = true)
    public Long SECONDARY_OR_SUPPLEMENTARY_RDS;
    public long UNMAPPED_READS;
    public long UNPAIRED_READ_DUPLICATES;
    public long READ_PAIR_DUPLICATES;
    public long READ_PAIR_OPTICAL_DUPLICATES;
    // formatted as "?" when the value is Double.NaN
    // https://github.com/samtools/htsjdk/blob/master/src/main/java/htsjdk/samtools/util/FormatUtil.java
    @Column(nullable = true)
    public Double PERCENT_DUPLICATION;
    public long ESTIMATED_LIBRARY_SIZE;


    public static DuplicationMetrics readFile(File file, String md5) throws FileNotFoundException, IllegalAccessException {
        Scanner scan = new Scanner(file);
        if (!scan.hasNext()) {
            System.err.println("Ignoring blank file: " + file);
            return null;
        }
        String firstLine = scan.nextLine();
        int headerRow;
        if (firstLine.contains("## htsjdk.samtools.metrics.StringHeader")) // Normal Picard first row
            headerRow = 6;
        else
            headerRow = 3;

        for (int i=1;i < headerRow;i++) {
            if (scan.hasNext()) {
                scan.nextLine(); // throw away
            } else {
                return null;
            }
        }

        String [] columnHeaders = scan.nextLine().split("\t");

        if (scan.hasNext()) {
            String [] parts = scan.nextLine().split("\t");
            DuplicationMetrics x = new DuplicationMetrics();
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
                    // if field is nullable ok else error
                    if ("java.lang.Double".equals(typeName))
                        continue;
                    else
                        return null;
                }

                if (typeName.equals("double"))
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