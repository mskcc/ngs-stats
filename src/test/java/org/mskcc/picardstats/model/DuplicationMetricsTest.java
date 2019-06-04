package org.mskcc.picardstats.model;

import org.junit.Ignore;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class DuplicationMetricsTest {

    @Ignore
    void readFile() throws FileNotFoundException, IllegalAccessException {
        // read file with wrong Picard stats format
        DuplicationMetrics.readFile(new File("src/test/resources/DIANA_0076_AHK5YCDMXX___hg19___MD.txt"), "");
    }
}