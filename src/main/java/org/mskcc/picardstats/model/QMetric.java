package org.mskcc.picardstats.model;

import javax.persistence.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

@Entity
/*
Internal MSK metric for oxidation.
 */
public class QMetric {
    @Id
    @Column(length = 150)
    public String filename;
    @Column(length = 32)
    public String md5RRS;

    @OneToOne
    @JoinColumn(name = "filename")
    private PicardFile picardFile;

    public double mskQ;

    public QMetric() {}

    public QMetric(String filename, Double mskQ, String md5) {
        this.filename = filename;
        this.md5RRS = md5;
        this.mskQ = mskQ;
    }

    public static QMetric readFile(File file, String md5) throws FileNotFoundException, IllegalAccessException {
        Scanner scan = new Scanner(file);
        String line = scan.nextLine();
        String [] parts = line.split("\t");
        if (parts.length == 4) {
            String value = parts[3];
            if (value.contains("."))
                return new QMetric(file.getName(), Double.parseDouble(value), md5);
            else
                return null; // for example "-Inf"
        } else
            return null;
    }
}