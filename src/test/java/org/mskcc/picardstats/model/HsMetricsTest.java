package org.mskcc.picardstats.model;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class HsMetricsTest {

    @Test
    void readFileMissingRequiredValue() throws FileNotFoundException, IllegalAccessException {
        File f = new File("src/test/resources/PITT_0234_AHVNVCBBXX___mm10___HS.txt");
        HsMetrics hs = HsMetrics.readFile(f, "3"); // required double FOLD_80_BASE_PENALTY has invalid value "?"
        assertNull(hs);
    }

    @Test
    void readFileFileWithAnotherQuestionMark() throws FileNotFoundException, IllegalAccessException {
        // File with FOLD_80_BASE_PENALTY=? so returns null and fails to parse
        File f = new File("src/test/resources/KIM_0748_BH3CGYBCX3___P07008___hg19___HS.txt");

        HsMetrics hs = HsMetrics.readFile(f, "3"); // required double FOLD_80_BASE_PENALTY has invalid value "?"
        assertNull(hs);
    }

    @Test
    void readFilePicard2_21_2() throws FileNotFoundException, IllegalAccessException {
        File f = new File("src/test/resources/JAX_0374_BHFFHMBBXY___P05240_W___P-01-WES_IGO_05240_W_1___hg19___2_21_2___HS.txt");
        HsMetrics hs = HsMetrics.readFile(f, "3");
        assertEquals("IDT_Exome_v1_FP_BAITS", hs.BAIT_SET);
        //MIN_TARGET_COVERAGE
        System.out.println(hs);
    }
}