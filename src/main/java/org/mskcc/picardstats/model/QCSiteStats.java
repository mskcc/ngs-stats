package org.mskcc.picardstats.model;

import lombok.ToString;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Columns from multiple Picard files displayed together on the QC site.
 */
@ResponseBody
@ToString
public class QCSiteStats {
    // Picard File
    private String run;
    private String request;
    private String sample;

    // ALIGNMENT SUMMARY METRICS
    public double PCT_ADAPTER;

    // Duplication Metrics
    public long READ_PAIRS_EXAMINED;
    public long UNMAPPED_READS;
    public Double PERCENT_DUPLICATION;

    // WGS
    public Double MEAN_COVERAGE;
    public Double PCT_10X;
    public Double PCT_30X;
    public Double PCT_100X;

    // RNASEQ
    public Double PCT_UTR_BASES;
    public Double PCT_INTRONIC_BASES;
    public Double PCT_INTERGENIC_BASES;

    // HS METRICS
    public double MEAN_TARGET_COVERAGE;
    public double ZERO_CVG_TARGETS_PCT;
    public double PCT_OFF_BAIT;


    public QCSiteStats() {}

    public QCSiteStats(PicardFile pf) {
        this.run = pf.getRun();
        this.request = pf.getRequest();
        this.sample = pf.getSample();
    }

    public void addAM(AlignmentSummaryMetrics am) {
        this.PCT_ADAPTER = am.PCT_ADAPTER;
    }

    public void addHS(HsMetrics hs) {
        this.MEAN_TARGET_COVERAGE = hs.MEAN_TARGET_COVERAGE;
        this.ZERO_CVG_TARGETS_PCT = hs.ZERO_CVG_TARGETS_PCT;
        this.PCT_OFF_BAIT = hs.PCT_OFF_BAIT;
    }

    public void addDM(DuplicationMetrics dm) {
        this.READ_PAIRS_EXAMINED = dm.READ_PAIRS_EXAMINED;
        this.UNMAPPED_READS = dm.UNMAPPED_READS;
        this.PERCENT_DUPLICATION = dm.PERCENT_DUPLICATION;
    }

    public void addWGS(WgsMetrics wgs) {
        this.MEAN_COVERAGE = wgs.MEAN_COVERAGE;
        this.PCT_10X = wgs.PCT_10X;
        this.PCT_30X = wgs.PCT_30X;
        this.PCT_100X = wgs.PCT_100X;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QCSiteStats that = (QCSiteStats) o;

        if (!run.equals(that.run)) return false;
        if (!request.equals(that.request)) return false;
        return sample.equals(that.sample);
    }

    @Override
    public int hashCode() {
        int result = run.hashCode();
        result = 31 * result + request.hashCode();
        result = 31 * result + sample.hashCode();
        return result;
    }
}