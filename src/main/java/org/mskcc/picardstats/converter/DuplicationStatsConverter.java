package org.mskcc.picardstats.converter;

import org.mskcc.domain.picardstats.DuplicationStats;
import org.mskcc.picardstats.model.DuplicationMetrics;
import org.springframework.stereotype.Component;

@Component
public class DuplicationStatsConverter {
    public DuplicationStats convert(DuplicationMetrics duplicationMetrics) {
        DuplicationStats duplicationStats = new DuplicationStats();

        if (duplicationMetrics == null)
            return duplicationStats;

        duplicationStats.setEstimatedLibrarySize(duplicationMetrics.ESTIMATED_LIBRARY_SIZE);
        duplicationStats.setFilename(duplicationMetrics.filename);
        duplicationStats.setLIBRARY(duplicationMetrics.LIBRARY);
        duplicationStats.setMd5RRS(duplicationMetrics.md5RRS);
        duplicationStats.setPercentDuplication(duplicationMetrics.PERCENT_DUPLICATION);
        duplicationStats.setReadPairDuplicates(duplicationMetrics.READ_PAIR_DUPLICATES);
        duplicationStats.setReadPairOpticalDuplicates(duplicationMetrics.READ_PAIR_OPTICAL_DUPLICATES);
        duplicationStats.setReadPairsExamined(duplicationMetrics.READ_PAIRS_EXAMINED);
        duplicationStats.setSecondaryOrSupplementaryRds(duplicationMetrics.SECONDARY_OR_SUPPLEMENTARY_RDS);
        duplicationStats.setUnmappedReads(duplicationMetrics.UNMAPPED_READS);
        duplicationStats.setUnpairedReadDuplicates(duplicationMetrics.UNPAIRED_READ_DUPLICATES);
        duplicationStats.setUnpairedReadsExamined(duplicationMetrics.UNPAIRED_READS_EXAMINED);

        return duplicationStats;
    }
}
