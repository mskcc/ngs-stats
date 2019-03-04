package org.mskcc.picardstats.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Columns from multiple Picard files displayed together on the QC site.
 */
@ResponseBody
@ToString
@Getter @Setter
public class QCSiteStats {
    // Picard File
    private String run;
    private String request;
    private String sample;

    // ALIGNMENT SUMMARY METRICS
    private double PCT_ADAPTER;
    private long unpairedReads; // derived field

    // Duplication Metrics
    private long READ_PAIRS_EXAMINED;
    private long UNMAPPED_READS;
    private Double PERCENT_DUPLICATION;

    // WGS
    private Double MEAN_COVERAGE;
    private Double PCT_10X;
    private Double PCT_30X;
    private Double PCT_100X;

    // RNASEQ
    private Double PCT_UTR_BASES;
    private Double PCT_INTRONIC_BASES;
    private Double PCT_INTERGENIC_BASES;

    // HS METRICS
    private double MEAN_TARGET_COVERAGE;
    private double ZERO_CVG_TARGETS_PCT;
    private double PCT_OFF_BAIT;


    public QCSiteStats() {}

    public QCSiteStats(PicardFile pf) {
        this.run = pf.getRun();
        this.request = pf.getRequest();
        this.sample = pf.getSample();
    }

    public void addAM(AlignmentSummaryMetrics am) {
        this.PCT_ADAPTER = am.PCT_ADAPTER;
        this.unpairedReads = am.getUnpairedReads();
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