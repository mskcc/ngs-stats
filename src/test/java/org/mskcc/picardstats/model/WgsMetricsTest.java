package org.mskcc.picardstats.model;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class WgsMetricsTest {

    @Test
    void readFilePicardVersion2_21_2() throws FileNotFoundException, IllegalAccessException {
        File f = new File("src/test/resources/MICHELLE_0189_BHMVTTDMXX_A1___P10519___day4_IGO_10519_14___hg19___WGS.txt");
        WgsMetrics wgs = WgsMetrics.readFile(f, "x");
        // no null pointer exception so all is good
        assertNotNull(wgs);
        System.out.println(wgs);
    }

    @Test
    void readFilePicardVersion() throws FileNotFoundException, IllegalAccessException {
        File f = new File("src/test/resources/DIANA_0279_AHTGNVDSXY___P11264_C___79_IGO_11264_C_40___GRCh37___WGS.txt");
        WgsMetrics wgs = WgsMetrics.readFile(f, "x");
        // no null pointer exception so all is good
        assertNotNull(wgs);
        System.out.println(wgs);
    }
}