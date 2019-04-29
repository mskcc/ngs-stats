package org.mskcc.picardstats.converter;

import org.mskcc.domain.picardstats.RnaSeqStats;
import org.mskcc.picardstats.model.RnaSeqMetrics;
import org.springframework.stereotype.Component;

@Component
public class RnaSeqStatsConverter {
    public RnaSeqStats convert(RnaSeqMetrics rnaSeqMetrics) {
        RnaSeqStats rnaSeqStats = new RnaSeqStats();

        if (rnaSeqMetrics == null)
            return rnaSeqStats;

        rnaSeqStats.setCodingBases(rnaSeqMetrics.CODING_BASES);
        rnaSeqStats.setCorrectStrandReads(rnaSeqMetrics.CORRECT_STRAND_READS);
        rnaSeqStats.setFilename(rnaSeqMetrics.filename);
        rnaSeqStats.setIgnoredReads(rnaSeqMetrics.IGNORED_READS);
        rnaSeqStats.setIncorrectStrandReads(rnaSeqMetrics.INCORRECT_STRAND_READS);
        rnaSeqStats.setIntergenicBases(rnaSeqMetrics.INTERGENIC_BASES);
        rnaSeqStats.setIntronicBases(rnaSeqMetrics.INTRONIC_BASES);
        rnaSeqStats.setMd5RRS(rnaSeqMetrics.md5RRS);
        rnaSeqStats.setMedian3PrimeBias(rnaSeqMetrics.MEDIAN_3PRIME_BIAS);
        rnaSeqStats.setMedian5PrimeBias(rnaSeqMetrics.MEDIAN_5PRIME_BIAS);
        rnaSeqStats.setMedian5PrimeTo3PrimeBias(rnaSeqMetrics.MEDIAN_5PRIME_TO_3PRIME_BIAS);
        rnaSeqStats.setMedianCvCoverage(rnaSeqMetrics.MEDIAN_CV_COVERAGE);
        rnaSeqStats.setNumR1TranscriptStrandReads(rnaSeqMetrics.NUM_R1_TRANSCRIPT_STRAND_READS);
        rnaSeqStats.setNumR2TranscriptStrandReads(rnaSeqMetrics.NUM_R2_TRANSCRIPT_STRAND_READS);
        rnaSeqStats.setNumUnexplainedReads(rnaSeqMetrics.NUM_UNEXPLAINED_READS);
        rnaSeqStats.setPctCodingBases(rnaSeqMetrics.PCT_CODING_BASES);
        rnaSeqStats.setPctCorrectStrandReads(rnaSeqMetrics.PCT_CORRECT_STRAND_READS);
        rnaSeqStats.setPctIntergenicBases(rnaSeqMetrics.PCT_INTERGENIC_BASES);
        rnaSeqStats.setPctIntronicBases(rnaSeqMetrics.PCT_INTRONIC_BASES);
        rnaSeqStats.setPctMrnaBases(rnaSeqMetrics.PCT_MRNA_BASES);
        rnaSeqStats.setPctR1TranscriptStrandReads(rnaSeqMetrics.PCT_R1_TRANSCRIPT_STRAND_READS);
        rnaSeqStats.setPctR2TranscriptStrandReads(rnaSeqMetrics.PCT_R2_TRANSCRIPT_STRAND_READS);
        rnaSeqStats.setPctRibosomalBases(rnaSeqMetrics.PCT_RIBOSOMAL_BASES);
        rnaSeqStats.setPctUsableBases(rnaSeqMetrics.PCT_USABLE_BASES);
        rnaSeqStats.setPctUtrBases(rnaSeqMetrics.PCT_UTR_BASES);
        rnaSeqStats.setPfAlignedBases(rnaSeqMetrics.PF_ALIGNED_BASES);
        rnaSeqStats.setPfBases(rnaSeqMetrics.PF_BASES);
        rnaSeqStats.setRibosomalBases(rnaSeqMetrics.RIBOSOMAL_BASES);
        rnaSeqStats.setUtrBases(rnaSeqMetrics.UTR_BASES);

        return rnaSeqStats;
    }
}
