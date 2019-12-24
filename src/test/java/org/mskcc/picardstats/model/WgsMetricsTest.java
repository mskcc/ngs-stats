package org.mskcc.picardstats.model;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WgsMetricsTest {

    @Test
    void readFilePicardVersion2_21_2() throws FileNotFoundException, IllegalAccessException {
        File f = new File("src/test/resources/MICHELLE_0189_BHMVTTDMXX_A1___P10519___day4_IGO_10519_14___hg19___WGS.txt");
        WgsMetrics wgs = WgsMetrics.readFile(f, "x");
        // no null pointer exception so all is good
        System.out.println(wgs);
    }
}