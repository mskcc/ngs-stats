package org.mskcc.sequencer.model;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ArchivedFastqTest {

    @Test
    void walkDirectory() throws IOException {
        List<ArchivedFastq> fastqs = ArchivedFastq.walkDirectory("src/test/resources/fastqArchive/",  "PITT_0999_AH5357BBXY_A2");
        assertEquals(2, fastqs.size());
        for (ArchivedFastq f : fastqs) {
            if (f.getProject() == null) { // Undetermined
                assertEquals("Undetermined_S0_R1_001.fastq.gz", new File(f.getFastq()).getName());
            } else {
                assertEquals("09245_E", f.getProject());
                assertEquals("S17-33024_IGO_09245_E_21", f.getSample());
                assertEquals("PITT_0999_AH5357BBXY_A2", f.getRunBaseDirectory());
                assertEquals("PITT_0999_AH5357BBXY", f.getRun());
            }
        }
    }
}