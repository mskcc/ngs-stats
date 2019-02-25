package org.mskcc.picardstats.model;

import javax.persistence.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Scanner;

@Entity
/*
https://github.com/broadinstitute/picard/blob/master/src/main/java/picard/analysis/CollectOxoGMetrics.java
 */
public class CpcgMetrics {
    @Id
    @Column(length = 150)
    public String filename;
    @Column(length = 32)
    public String md5RRS;

    /** The name of the sample being assayed. */
    public String SAMPLE_ALIAS;
    /** The name of the library being assayed. */
    public String LIBRARY;
    /** The sequence context being reported on. */
    public String CONTEXT;
    /** The total number of sites that had at least one base covering them. */
    public int TOTAL_SITES;
    /** The total number of basecalls observed at all sites. */
    public long TOTAL_BASES;
    /** The number of reference alleles observed as C in read 1 and G in read 2. */
    public long REF_NONOXO_BASES;
    /** The number of reference alleles observed as G in read 1 and C in read 2. */
    public long REF_OXO_BASES;
    /** The total number of reference alleles observed */
    public long REF_TOTAL_BASES;
    /**
     * The count of observed A basecalls at C reference positions and T basecalls
     * at G reference bases that are correlated to instrument read number in a way
     * that rules out oxidation as the cause
     */
    public long ALT_NONOXO_BASES;
    /**
     * The count of observed A basecalls at C reference positions and T basecalls
     * at G reference bases that are correlated to instrument read number in a way
     * that is consistent with oxidative damage.
     */
    public long ALT_OXO_BASES;
    /** The oxo error rate, calculated as max(ALT_OXO_BASES - ALT_NONOXO_BASES, 1) / TOTAL_BASES */
    public double OXIDATION_ERROR_RATE;
    /** -10 * log10(OXIDATION_ERROR_RATE) */
    public double OXIDATION_Q;

    // Fields below this point are metrics that are calculated to see if there is oxidative damage that is
    // biased toward the reference base - i.e. occurs more where the reference base is a C vs. a G or vice
    // versa.

    /** The number of ref basecalls observed at sites where the genome reference == C. */
    public long C_REF_REF_BASES;
    /** The number of ref basecalls observed at sites where the genome reference == G. */
    public long G_REF_REF_BASES;
    /** The number of alt (A/T) basecalls observed at sites where the genome reference == C. */
    public long C_REF_ALT_BASES;
    /** The number of alt (A/T) basecalls observed at sites where the genome reference == G. */
    public long G_REF_ALT_BASES;

    /**
     * The rate at which C>A and G>T substitutions are observed at C reference sites above the expected rate if there
     * were no bias between sites with a C reference base vs. a G reference base.
     */
    public double C_REF_OXO_ERROR_RATE;
    /** C_REF_OXO_ERROR_RATE expressed as a phred-scaled quality score. */
    public double C_REF_OXO_Q;
    /**
     * The rate at which C>A and G>T substitutions are observed at G reference sites above the expected rate if there
     * were no bias between sites with a C reference base vs. a G reference base.
     */
    public double G_REF_OXO_ERROR_RATE;
    /** G_REF_OXO_ERROR_RATE expressed as a phred-scaled quality score. */
    public double G_REF_OXO_Q;


    public static int headerRow = 6;
    public static CpcgMetrics readFile(File file, String md5) throws FileNotFoundException, IllegalAccessException {
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
            CpcgMetrics x = new CpcgMetrics();
            x.filename = file.getName();
            x.md5RRS = md5;
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
                    field.setDouble(x, Double.parseDouble(value));
                else if (field.getType().getName().equals("int"))
                    field.set(x, Integer.parseInt(value));
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