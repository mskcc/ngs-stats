package org.mskcc.picardstats;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class PicardStatsControllerTest {

    @Test
    void updateDatabaseFileFilter() throws Exception {
        File tempFile = File.createTempFile("ShortlivedNewFile", ".txt");
        PicardStatsController.DaysFileFilter dff = new PicardStatsController.DaysFileFilter(1);

        File tempDir = new File(System.getProperty("java.io.tmpdir"));
        File [] tempFiles = tempDir.listFiles(dff);
        for (File f : tempFiles) {
            if (f.getName().contains("ShortlivedNewFile"))
                return;
        }
        throw new Exception("Filter failed to include the file");
    }
}