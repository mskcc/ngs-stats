package org.mskcc.sequencer.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StartStopSequencerTest {

    @Test
    void removeDateFromRun() {
        String run = StartStopSequencer.removeDateFromRun("181030_MOMO_0301_AHN33WBCX2");
        assertEquals("MOMO_0301_AHN33WBCX2", run);
    }
}