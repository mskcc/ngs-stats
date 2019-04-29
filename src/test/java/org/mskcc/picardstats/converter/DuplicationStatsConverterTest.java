package org.mskcc.picardstats.converter;


import org.junit.jupiter.api.Test;
import org.mskcc.domain.picardstats.DuplicationStats;
import org.mskcc.picardstats.model.DuplicationMetrics;

import static org.assertj.core.api.Assertions.assertThat;


public class DuplicationStatsConverterTest {
    private DuplicationStatsConverter converter = new DuplicationStatsConverter();

    @Test
    public void whenDuplicationMetricsAreConverted_shouldReturnDuplicationStats() throws Exception {
        //given
        DuplicationMetrics dm = new DuplicationMetrics();
        dm.ESTIMATED_LIBRARY_SIZE = 4324;
        dm.filename = "ndjfndsjkf.txt";
        dm.LIBRARY = "gmdkq";
        dm.md5RRS = "432fs";
        dm.PERCENT_DUPLICATION = 5.6;
        dm.READ_PAIR_DUPLICATES = 3432;
        dm.READ_PAIR_OPTICAL_DUPLICATES = 43;
        dm.READ_PAIRS_EXAMINED = 32432;
        dm.SECONDARY_OR_SUPPLEMENTARY_RDS = 4324L;
        dm.UNMAPPED_READS = 3234;
        dm.UNPAIRED_READ_DUPLICATES = 65432;
        dm.UNPAIRED_READS_EXAMINED = 333;

        //when
        DuplicationStats ds = converter.convert(dm);

        //then
        assertThat(ds.getEstimatedLibrarySize()).isEqualTo(dm.ESTIMATED_LIBRARY_SIZE);
        assertThat(ds.getFilename()).isEqualTo(dm.filename);
        assertThat(ds.getLIBRARY()).isEqualTo(dm.LIBRARY);
        assertThat(ds.getMd5RRS()).isEqualTo(dm.md5RRS);
        assertThat(ds.getPercentDuplication()).isEqualTo(dm.PERCENT_DUPLICATION);
        assertThat(ds.getReadPairDuplicates()).isEqualTo(dm.READ_PAIR_DUPLICATES);
        assertThat(ds.getReadPairOpticalDuplicates()).isEqualTo(dm.READ_PAIR_OPTICAL_DUPLICATES);
        assertThat(ds.getReadPairsExamined()).isEqualTo(dm.READ_PAIRS_EXAMINED);
        assertThat(ds.getSecondaryOrSupplementaryRds()).isEqualTo(dm.SECONDARY_OR_SUPPLEMENTARY_RDS);
        assertThat(ds.getUnmappedReads()).isEqualTo(dm.UNMAPPED_READS);
        assertThat(ds.getUnpairedReadDuplicates()).isEqualTo(dm.UNPAIRED_READ_DUPLICATES);
        assertThat(ds.getUnpairedReadsExamined()).isEqualTo(dm.UNPAIRED_READS_EXAMINED);
    }

}