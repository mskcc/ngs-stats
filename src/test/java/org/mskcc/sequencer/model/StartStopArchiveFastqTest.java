package org.mskcc.sequencer.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StartStopArchiveFastqTest {

    @Test
    void getSequencerName() {
        String seq = StartStopArchiveFastq.getSequencerName("MOMO_0304_BHNHWJBCX2");
        assertEquals("momo", seq);
    }

    @Test
    void getRun() {
        String run = StartStopArchiveFastq.getRun("PITT_0328_AH5357BBXY_A1");
        assertEquals("PITT_0328_AH5357BBXY", run);

        String runB = StartStopArchiveFastq.getRun("PITT_0328_AH5357BBXY");
        assertEquals("PITT_0328_AH5357BBXY", runB);
    }
}