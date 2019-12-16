package org.mskcc.picardstats.model;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class RnaSeqMetricsTest {

    @Test
    void readFilePicard2_21_2() throws FileNotFoundException, IllegalAccessException {
        File f = new File("src/test/resources/JAX_0374_BHFFHMBBXY___P09769_B___21_6_IGO_09769_B_64___hg19___2_21_2___RNA.txt");
        RnaSeqMetrics r = RnaSeqMetrics.readFile(f, "3");
        assertEquals(21320107372L, r.PF_BASES);
        assertEquals(327951L, r.NUM_UNEXPLAINED_READS);
        System.out.println(r);
    }
}