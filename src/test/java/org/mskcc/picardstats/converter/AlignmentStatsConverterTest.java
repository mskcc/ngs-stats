package org.mskcc.picardstats.converter;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mskcc.domain.picardstats.AlignmentStats;
import org.mskcc.picardstats.model.AlignmentSummaryMetrics;

public class AlignmentStatsConverterTest {
    private AlignmentStatsConverter converter = new AlignmentStatsConverter();

    @Test
    public void whenAlignmentMetricsAreConverted_shouldCreateAlignmentStats() throws Exception {
        //given
        AlignmentSummaryMetrics metrics = new AlignmentSummaryMetrics();
        metrics.BAD_CYCLES = 123;
        metrics.CATEGORY = AlignmentStats.Category.FIRST_OF_PAIR;
        metrics.filename = "Blabla.txt";
        metrics.md5RRS = "123dsa";
        metrics.MEAN_READ_LENGTH = 12.4;
        metrics.PCT_ADAPTER = 4.5;
        metrics.PCT_CHIMERAS = 2.3;
        metrics.PCT_PF_READS = 5.6;
        metrics.PCT_PF_READS_ALIGNED = 7.6;
        metrics.PCT_PF_READS_IMPROPER_PAIRS = 9.5;
        metrics.PCT_READS_ALIGNED_IN_PAIRS = 32.5;
        metrics.PF_ALIGNED_BASES = 100;
        metrics.PF_HQ_ALIGNED_BASES = 45;
        metrics.PF_HQ_ALIGNED_Q20_BASES = 34;
        metrics.PF_HQ_ALIGNED_READS = 67;
        metrics.PF_HQ_ERROR_RATE = 1.34;
        metrics.PF_HQ_MEDIAN_MISMATCHES = 9.6;
        metrics.PF_INDEL_RATE = 10.5;
        metrics.PF_MISMATCH_RATE = 4.5;
        metrics.PF_NOISE_READS = 60000;
        metrics.PF_READS = 56;
        metrics.PF_READS_ALIGNED = 34343;
        metrics.PF_READS_IMPROPER_PAIRS = 100;
        metrics.READS_ALIGNED_IN_PAIRS = 4545;
        metrics.STRAND_BALANCE = 2323.3;
        metrics.TOTAL_READS = 100000002;

        //when
        AlignmentStats stats = converter.convert(metrics);

        //then
        Assertions.assertThat(stats.getBadCycles()).isEqualTo(metrics.BAD_CYCLES);
        Assertions.assertThat(stats.getCategory()).isEqualTo(metrics.CATEGORY);
        Assertions.assertThat(stats.getFilename()).isEqualTo(metrics.filename);
        Assertions.assertThat(stats.getMd5RRS()).isEqualTo(metrics.md5RRS);
        Assertions.assertThat(stats.getMeanReadLength()).isEqualTo(metrics.MEAN_READ_LENGTH);
        Assertions.assertThat(stats.getPctAdapter()).isEqualTo(metrics.PCT_ADAPTER);
        Assertions.assertThat(stats.getPctChimeras()).isEqualTo(metrics.PCT_CHIMERAS);
        Assertions.assertThat(stats.getPctPfReads()).isEqualTo(metrics.PCT_PF_READS);
        Assertions.assertThat(stats.getPctPfReadsAligned()).isEqualTo(metrics.PCT_PF_READS_ALIGNED);
        Assertions.assertThat(stats.getPctPfReadsImproperPairs()).isEqualTo(metrics.PCT_PF_READS_IMPROPER_PAIRS);
        Assertions.assertThat(stats.getPctReadsAlignedInPairs()).isEqualTo(metrics.PCT_READS_ALIGNED_IN_PAIRS);
        Assertions.assertThat(stats.getPfAlignedBases()).isEqualTo(metrics.PF_ALIGNED_BASES);
        Assertions.assertThat(stats.getPfHqAlignedBases()).isEqualTo(metrics.PF_HQ_ALIGNED_BASES);
        Assertions.assertThat(stats.getPfHqAlignedQ20Bases()).isEqualTo(metrics.PF_HQ_ALIGNED_Q20_BASES);
        Assertions.assertThat(stats.getPfHqAlignedReads()).isEqualTo(metrics.PF_HQ_ALIGNED_READS);
        Assertions.assertThat(stats.getPfHqErrorRate()).isEqualTo(metrics.PF_HQ_ERROR_RATE);
        Assertions.assertThat(stats.getPfHqMedianMismatches()).isEqualTo(metrics.PF_HQ_MEDIAN_MISMATCHES);
        Assertions.assertThat(stats.getPfIndelRate()).isEqualTo(metrics.PF_INDEL_RATE);
        Assertions.assertThat(stats.getPfMismatchRate()).isEqualTo(metrics.PF_MISMATCH_RATE);
        Assertions.assertThat(stats.getPfNoiseReads()).isEqualTo(metrics.PF_NOISE_READS);
        Assertions.assertThat(stats.getPfReads()).isEqualTo(metrics.PF_READS);
        Assertions.assertThat(stats.getPfReadsAligned()).isEqualTo(metrics.PF_READS_ALIGNED);
        Assertions.assertThat(stats.getPfReadsImproperPairs()).isEqualTo(metrics.PF_READS_IMPROPER_PAIRS);
        Assertions.assertThat(stats.getReadsAlignedInPairs()).isEqualTo(metrics.READS_ALIGNED_IN_PAIRS);
        Assertions.assertThat(stats.getStrandBalance()).isEqualTo(metrics.STRAND_BALANCE);
        Assertions.assertThat(stats.getTotalReads()).isEqualTo(metrics.TOTAL_READS);
    }

}