package org.mskcc.picardstats.converter;

import org.mskcc.domain.picardstats.CpcgStats;
import org.mskcc.picardstats.model.CpcgMetrics;
import org.springframework.stereotype.Component;

@Component
public class CpcgStatsConverter {
    public CpcgStats convert(CpcgMetrics cpcgMetrics) {
        CpcgStats cpcgStats = new CpcgStats();

        if (cpcgMetrics == null)
            return cpcgStats;

        cpcgStats.setAltNonoxoBases(cpcgMetrics.ALT_NONOXO_BASES);
        cpcgStats.setAltOxoBases(cpcgMetrics.ALT_OXO_BASES);
        cpcgStats.setContext(cpcgMetrics.CONTEXT);
        cpcgStats.setCRefAltBases(cpcgMetrics.C_REF_ALT_BASES);
        cpcgStats.setCRefOxoErrorRate(cpcgMetrics.C_REF_OXO_ERROR_RATE);
        cpcgStats.setCRefOxoQ(cpcgMetrics.C_REF_OXO_Q);
        cpcgStats.setCRefRefBases(cpcgMetrics.C_REF_REF_BASES);
        cpcgStats.setFilename(cpcgMetrics.filename);
        cpcgStats.setGRefAltBases(cpcgMetrics.G_REF_ALT_BASES);
        cpcgStats.setGRefOxoErrorRate(cpcgMetrics.G_REF_OXO_ERROR_RATE);
        cpcgStats.setGRefOxoQ(cpcgMetrics.G_REF_OXO_Q);
        cpcgStats.setGRefRefBases(cpcgMetrics.G_REF_REF_BASES);
        cpcgStats.setLibrary(cpcgMetrics.LIBRARY);
        cpcgStats.setMd5RRS(cpcgMetrics.md5RRS);
        cpcgStats.setOxidationErrorRate(cpcgMetrics.OXIDATION_ERROR_RATE);
        cpcgStats.setOxidationQ(cpcgMetrics.OXIDATION_Q);
        cpcgStats.setRefNonoxoBases(cpcgMetrics.REF_NONOXO_BASES);
        cpcgStats.setRefOxoBases(cpcgMetrics.REF_OXO_BASES);
        cpcgStats.setRefTotalBases(cpcgMetrics.REF_TOTAL_BASES);
        cpcgStats.setSampleAlias(cpcgMetrics.SAMPLE_ALIAS);
        cpcgStats.setTotalBases(cpcgMetrics.TOTAL_BASES);
        cpcgStats.setTotalSites(cpcgMetrics.TOTAL_SITES);

        return cpcgStats;
    }
}
