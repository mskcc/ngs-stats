package org.mskcc.picardstats.model;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class DuplicationMetricsTest {

    @Test
    void readFileBlank() throws FileNotFoundException, IllegalAccessException {
        // read file with wrong Picard stats format
        DuplicationMetrics dm = DuplicationMetrics.readFile(new File("src/test/resources/DIANA_0008_AH3V2JDMXX___BLANK_FILE___hg19___MD.txt"), "");
    }


    @Test
    void readFileMET() throws FileNotFoundException, IllegalAccessException {
        // read file with wrong Picard stats format
        DuplicationMetrics dm = DuplicationMetrics.readFile(new File("src/test/resources/DIANA_0076_AHK5YCDMXX___hg19___MD.txt"), "");
        assertEquals(1254120L, dm.UNPAIRED_READS_EXAMINED);
    }

    @Test
    void readFileNormal() throws FileNotFoundException, IllegalAccessException {
        // read file with wrong Picard stats format
        DuplicationMetrics dm = DuplicationMetrics.readFile(new File("src/test/resources/DIANA_0008_AH3V2JDMXX___P07951_I___P-0003659-N01-WES_IGO_07951_I_3___hg19___MD.txt"), "");
        assertEquals(405498L, dm.UNPAIRED_READS_EXAMINED);
    }

    @Test
    void readFileNormalPicard2_21_2() throws FileNotFoundException, IllegalAccessException {
        DuplicationMetrics dm = DuplicationMetrics.readFile(new File("src/test/resources/JAX_0374_BHFFHMBBXY___P05240_W___P-000-N01-WES_IGO_05240_W_1___hg19___2_21_2___MD.txt"), "");
        assertEquals(247441262L, dm.ESTIMATED_LIBRARY_SIZE);
        System.out.println(dm);
        // TODO
    }
}