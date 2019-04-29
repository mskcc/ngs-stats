package org.mskcc.picardstats.converter;

import org.mskcc.domain.picardstats.AlignmentStats;
import org.mskcc.picardstats.model.AlignmentSummaryMetrics;
import org.springframework.stereotype.Component;

@Component
public class AlignmentStatsConverter {
    public AlignmentStats convert(AlignmentSummaryMetrics alignmentSummaryMetrics) {
        AlignmentStats alignmentStats = new AlignmentStats();

        if (alignmentSummaryMetrics == null)
            return alignmentStats;

        alignmentStats.setBadCycles(alignmentSummaryMetrics.BAD_CYCLES);
        alignmentStats.setCategory(alignmentSummaryMetrics.CATEGORY);
        alignmentStats.setFilename(alignmentSummaryMetrics.filename);
        alignmentStats.setMd5RRS(alignmentSummaryMetrics.md5RRS);
        alignmentStats.setMeanReadLength(alignmentSummaryMetrics.MEAN_READ_LENGTH);
        alignmentStats.setPctAdapter(alignmentSummaryMetrics.PCT_ADAPTER);
        alignmentStats.setPctChimeras(alignmentSummaryMetrics.PCT_CHIMERAS);
        alignmentStats.setPctPfReads(alignmentSummaryMetrics.PCT_PF_READS);
        alignmentStats.setPctPfReadsAligned(alignmentSummaryMetrics.PCT_PF_READS_ALIGNED);
        alignmentStats.setPctPfReadsImproperPairs(alignmentSummaryMetrics.PCT_PF_READS_IMPROPER_PAIRS);
        alignmentStats.setPctReadsAlignedInPairs(alignmentSummaryMetrics.PCT_READS_ALIGNED_IN_PAIRS);
        alignmentStats.setPfAlignedBases(alignmentSummaryMetrics.PF_ALIGNED_BASES);
        alignmentStats.setPfHqAlignedBases(alignmentSummaryMetrics.PF_HQ_ALIGNED_BASES);
        alignmentStats.setPfHqAlignedQ20Bases(alignmentSummaryMetrics.PF_HQ_ALIGNED_Q20_BASES);
        alignmentStats.setPfHqAlignedReads(alignmentSummaryMetrics.PF_HQ_ALIGNED_READS);
        alignmentStats.setPfHqErrorRate(alignmentSummaryMetrics.PF_HQ_ERROR_RATE);
        alignmentStats.setPfHqMedianMismatches(alignmentSummaryMetrics.PF_HQ_MEDIAN_MISMATCHES);
        alignmentStats.setPfIndelRate(alignmentSummaryMetrics.PF_INDEL_RATE);
        alignmentStats.setPfMismatchRate(alignmentSummaryMetrics.PF_MISMATCH_RATE);
        alignmentStats.setPfNoiseReads(alignmentSummaryMetrics.PF_NOISE_READS);
        alignmentStats.setPfReads(alignmentSummaryMetrics.PF_READS);
        alignmentStats.setPfReadsAligned(alignmentSummaryMetrics.PF_READS_ALIGNED);
        alignmentStats.setPfReadsImproperPairs(alignmentSummaryMetrics.PF_READS_IMPROPER_PAIRS);
        alignmentStats.setReadsAlignedInPairs(alignmentSummaryMetrics.READS_ALIGNED_IN_PAIRS);
        alignmentStats.setStrandBalance(alignmentSummaryMetrics.STRAND_BALANCE);
        alignmentStats.setTotalReads(alignmentSummaryMetrics.TOTAL_READS);

        return alignmentStats;
    }
}
