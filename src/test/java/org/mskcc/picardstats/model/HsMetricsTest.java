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
}