package org.mskcc.picardstats.converter;

import org.junit.jupiter.api.Test;
import org.mskcc.domain.picardstats.CpcgStats;
import org.mskcc.picardstats.model.CpcgMetrics;


public class CpcgStatsConverterTest {
    private CpcgStatsConverter converter = new CpcgStatsConverter();

    @Test
    public void whenCpcgMetricsAreConverted_shouldReturnCscgStats() throws Exception {
        //given
        CpcgMetrics cpcgMetrics = new CpcgMetrics();
        cpcgMetrics.ALT_NONOXO_BASES = 214242l;
        cpcgMetrics.ALT_OXO_BASES = 43432;
        cpcgMetrics.C_REF_ALT_BASES = 432432l;
        cpcgMetrics.C_REF_OXO_ERROR_RATE = 432;


        //when
        CpcgStats stats = converter.convert(cpcgMetrics);


        //then

    }

}