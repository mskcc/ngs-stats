package org.mskcc.picardstats.converter;

import org.mskcc.domain.picardstats.WgsStats;
import org.mskcc.picardstats.model.WgsMetrics;
import org.springframework.stereotype.Component;

@Component
public class WgsStatsConverter {
    public WgsStats convert(WgsMetrics wgsMetrics) {
        WgsStats wgsStats = new WgsStats();

        if (wgsMetrics == null)
            return wgsStats;

        wgsStats.setFilename(wgsMetrics.filename);
        wgsStats.setGenomeTerritory(wgsMetrics.GENOME_TERRITORY);
        wgsStats.setHetSnpQ(wgsMetrics.HET_SNP_Q);
        wgsStats.setHetSnpSensitivity(wgsMetrics.HET_SNP_SENSITIVITY);
        wgsStats.setMadCoverage(wgsMetrics.MAD_COVERAGE);
        wgsStats.setMd5RRS(wgsMetrics.md5RRS);
        wgsStats.setMeanCoverage(wgsMetrics.MEAN_COVERAGE);
        wgsStats.setMedianCoverage(wgsMetrics.MEDIAN_COVERAGE);
        wgsStats.setPct1X(wgsMetrics.PCT_1X);
        wgsStats.setPct5X(wgsMetrics.PCT_5X);
        wgsStats.setPct10X(wgsMetrics.PCT_10X);
        wgsStats.setPct15X(wgsMetrics.PCT_15X);
        wgsStats.setPct20X(wgsMetrics.PCT_20X);
        wgsStats.setPct25X(wgsMetrics.PCT_25X);
        wgsStats.setPct30X(wgsMetrics.PCT_30X);
        wgsStats.setPct40X(wgsMetrics.PCT_40X);
        wgsStats.setPct50X(wgsMetrics.PCT_50X);
        wgsStats.setPct60X(wgsMetrics.PCT_60X);
        wgsStats.setPct70X(wgsMetrics.PCT_70X);
        wgsStats.setPct80X(wgsMetrics.PCT_80X);
        wgsStats.setPct90X(wgsMetrics.PCT_90X);
        wgsStats.setPct100X(wgsMetrics.PCT_100X);
        wgsStats.setPctExcBaseq(wgsMetrics.PCT_EXC_BASEQ);
        wgsStats.setPctExcCapped(wgsMetrics.PCT_EXC_CAPPED);
        wgsStats.setPctExcDupe(wgsMetrics.PCT_EXC_DUPE);
        wgsStats.setPctExcMapq(wgsMetrics.PCT_EXC_MAPQ);
        wgsStats.setPctExcOverlap(wgsMetrics.PCT_EXC_OVERLAP);
        wgsStats.setPctExcTotal(wgsMetrics.PCT_EXC_TOTAL);
        wgsStats.setPctExcUnpaired(wgsMetrics.PCT_EXC_UNPAIRED);
        wgsStats.setSdCoverage(wgsMetrics.SD_COVERAGE);

        return wgsStats;
    }
}
