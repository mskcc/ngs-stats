package org.mskcc.picardstats.converter;

import org.mskcc.domain.picardstats.HsStats;
import org.mskcc.picardstats.model.HsMetrics;
import org.springframework.stereotype.Component;

@Component
public class HsStatsConverter {
    public HsStats convert(HsMetrics hsMetrics) {
        HsStats hsStats = new HsStats();

        if (hsMetrics == null)
            return hsStats;

        hsStats.setAtDropout(hsMetrics.AT_DROPOUT);
        hsStats.setBaitDesignEfficiency(hsMetrics.BAIT_DESIGN_EFFICIENCY);
        hsStats.setBaitSet(hsMetrics.BAIT_SET);
        hsStats.setBaitTerritory(hsMetrics.BAIT_TERRITORY);
        hsStats.setFilename(hsMetrics.filename);
        hsStats.setFold80BasePenalty(hsMetrics.FOLD_80_BASE_PENALTY);
        hsStats.setFoldEnrichment(hsMetrics.FOLD_ENRICHMENT);
        hsStats.setGcDropout(hsMetrics.GC_DROPOUT);
        hsStats.setGenomeSize(hsMetrics.GENOME_SIZE);
        hsStats.setHetSnpQ(hsMetrics.HET_SNP_Q);
        hsStats.setHetSnpSensitivity(hsMetrics.HET_SNP_SENSITIVITY);
        hsStats.setHsLibrarySize(hsMetrics.HS_LIBRARY_SIZE);
        hsStats.setHsPenalty10X(hsMetrics.HS_PENALTY_10X);
        hsStats.setHsPenalty20X(hsMetrics.HS_PENALTY_20X);
        hsStats.setHsPenalty30X(hsMetrics.HS_PENALTY_30X);
        hsStats.setHsPenalty40X(hsMetrics.HS_PENALTY_40X);
        hsStats.setHsPenalty50X(hsMetrics.HS_PENALTY_50X);
        hsStats.setHsPenalty100X(hsMetrics.HS_PENALTY_100X);
        hsStats.setMaxTargetCoverage(hsMetrics.MAX_TARGET_COVERAGE);
        hsStats.setMd5RRS(hsMetrics.md5RRS);
        hsStats.setMeanBaitCoverage(hsMetrics.MEAN_BAIT_COVERAGE);
        hsStats.setMeanTargetCoverage(hsMetrics.MEAN_TARGET_COVERAGE);
        hsStats.setMedianTargetCoverage(hsMetrics.MEDIAN_TARGET_COVERAGE);
        hsStats.setNearBaitBases(hsMetrics.NEAR_BAIT_BASES);
        hsStats.setOffBaitBases(hsMetrics.OFF_BAIT_BASES);
        hsStats.setOnBaitVsSelected(hsMetrics.ON_BAIT_VS_SELECTED);
        hsStats.setOnTargetBases(hsMetrics.ON_TARGET_BASES);
        hsStats.setPctExcBaseq(hsMetrics.PCT_EXC_BASEQ);
        hsStats.setPctExcDupe(hsMetrics.PCT_EXC_DUPE);
        hsStats.setPctExcMapq(hsMetrics.PCT_EXC_MAPQ);
        hsStats.setPctExcOffTarget(hsMetrics.PCT_EXC_OFF_TARGET);
        hsStats.setPctExcOverlap(hsMetrics.PCT_EXC_OVERLAP);
        hsStats.setPctOffBait(hsMetrics.PCT_OFF_BAIT);
        hsStats.setPctPfReads(hsMetrics.PCT_PF_READS);
        hsStats.setPctPfUqReads(hsMetrics.PCT_PF_UQ_READS);
        hsStats.setPctPfUqReadsAligned(hsMetrics.PCT_PF_UQ_READS_ALIGNED);
        hsStats.setPctSelectedBases(hsMetrics.PCT_SELECTED_BASES);
        hsStats.setPctTargetBases1X(hsMetrics.PCT_TARGET_BASES_1X);
        hsStats.setPctTargetBases2X(hsMetrics.PCT_TARGET_BASES_2X);
        hsStats.setPctTargetBases10X(hsMetrics.PCT_TARGET_BASES_10X);
        hsStats.setPctTargetBases20X(hsMetrics.PCT_TARGET_BASES_20X);
        hsStats.setPctTargetBases30X(hsMetrics.PCT_TARGET_BASES_30X);
        hsStats.setPctTargetBases40X(hsMetrics.PCT_TARGET_BASES_40X);
        hsStats.setPctTargetBases50X(hsMetrics.PCT_TARGET_BASES_50X);
        hsStats.setPctTargetBases100X(hsMetrics.PCT_TARGET_BASES_100X);
        hsStats.setPctUsableBasesOnBait(hsMetrics.PCT_USABLE_BASES_ON_BAIT);
        hsStats.setPctUsableBasesOnTarget(hsMetrics.PCT_USABLE_BASES_ON_TARGET);
        hsStats.setPfBasesAligned(hsMetrics.PF_BASES_ALIGNED);
        hsStats.setPfReads(hsMetrics.PF_READS);
        hsStats.setPfUniqueReads(hsMetrics.PF_UNIQUE_READS);
        hsStats.setPfUqBasesAligned(hsMetrics.PF_UQ_BASES_ALIGNED);
        hsStats.setPfUqReadsAligned(hsMetrics.PF_UQ_READS_ALIGNED);
        hsStats.setTargetTerritory(hsMetrics.TARGET_TERRITORY);
        hsStats.setTotalReads(hsMetrics.TOTAL_READS);
        hsStats.setZeroCvgTargetsPct(hsMetrics.ZERO_CVG_TARGETS_PCT);

        return hsStats;
    }
}
