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

    @Test
    void getSampleNamePostJan2016() {
        ArchivedFastq x = new ArchivedFastq();
        x.setSample("hello_IGO_there");
        assertEquals("hello", x.getSampleName());
    }

    @Test
    void getSampleNameOrig() {
        ArchivedFastq x = new ArchivedFastq();
        x.setSample("hello");
        assertEquals("hello", x.getSampleName());
    }

    @Test
    void getSampleName() {
        String result = ArchivedFastq.getSampleName("065RA_DLP_UNSORTED_128655A_23_51_IGO_11113_C_2_1_631_S631_L003_R1_001.fastq.gz");
        assertEquals("065RA_DLP_UNSORTED_128655A_23_51_IGO_11113_C_2_1_631", result);

        String result2 = ArchivedFastq.getSampleName("065RA_DLP_UNSORTED_128655A_23_51_IGO_11113_C_2_1_631_S63_L007_R2_001.fastq.gz");
        assertEquals("065RA_DLP_UNSORTED_128655A_23_51_IGO_11113_C_2_1_631", result2);
    }
}