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
    private String referenceGenome;

    public Long TOTAL_READS;
    public Long PF_READS_ALIGNED;
    public Long READS_ALIGNED_IN_PAIRS;
    public AlignmentSummaryMetrics.Category category;
    // ALIGNMENT SUMMARY METRICS
    private Double PCT_ADAPTER;
    private Long unpairedReads; // derived field
    // Duplication Metrics
    private Long READ_PAIRS_EXAMINED;
    private Long READ_PAIRS_DUPLICATES;
    private Long UNMAPPED_READS;
    private Double PERCENT_DUPLICATION;

    // WGS
    private Double MEAN_COVERAGE;
    private Double PCT_10X;
    private Double PCT_30X;
    private Double PCT_100X;
    private Long GENOME_TERRITORY;

    // RNASEQ
    private Double PCT_UTR_BASES;
    private Double PCT_INTRONIC_BASES;
    private Double PCT_INTERGENIC_BASES;
    private Double PCT_RIBOSOMAL_BASES;
    private Double PCT_CODING_BASES;
    private Double PCT_MRNA_BASES;

    // HS METRICS
    private Double MEAN_TARGET_COVERAGE;
    private Double ZERO_CVG_TARGETS_PCT;
    private Double PCT_OFF_BAIT;
    private String BAIT_SET;
    private Double PCT_TARGET_BASES_10X;
    private Double PCT_TARGET_BASES_30X;
    private Double PCT_TARGET_BASES_100X;

    //Q Metrics
    private Double MSK_Q;

    //CPCG METRICS
    private Double G_REF_OXO_Q;

    public QCSiteStats() {}

    public QCSiteStats(PicardFile pf) {
        this.run = pf.getRun();
        this.request = pf.getRequest();
        this.sample = pf.getSample();
        this.referenceGenome = pf.getReferenceGenome();
    }

    public void addAM(AlignmentSummaryMetrics am) {
        this.PCT_ADAPTER = am.PCT_ADAPTER;
        this.unpairedReads = am.getUnpairedReads();
        this.TOTAL_READS = am.TOTAL_READS;
        this.PF_READS_ALIGNED = am.PF_READS_ALIGNED;
        this.READS_ALIGNED_IN_PAIRS = am.READS_ALIGNED_IN_PAIRS;
        this.category = am.CATEGORY;
    }

    public void addHS(HsMetrics hs) {
        this.MEAN_TARGET_COVERAGE = hs.MEAN_TARGET_COVERAGE;
        this.ZERO_CVG_TARGETS_PCT = hs.ZERO_CVG_TARGETS_PCT;
        this.PCT_OFF_BAIT = hs.PCT_OFF_BAIT;
        this.BAIT_SET = hs.BAIT_SET;
        this.PCT_TARGET_BASES_10X = hs.PCT_TARGET_BASES_10X;
        this.PCT_TARGET_BASES_30X = hs.PCT_TARGET_BASES_30X;
        this.PCT_TARGET_BASES_100X = hs.PCT_TARGET_BASES_100X;
    }

    public void addDM(DuplicationMetrics dm) {
        this.READ_PAIRS_EXAMINED = dm.READ_PAIRS_EXAMINED;
        this.UNMAPPED_READS = dm.UNMAPPED_READS;
        this.PERCENT_DUPLICATION = dm.PERCENT_DUPLICATION;
        this.READ_PAIRS_DUPLICATES = dm.READ_PAIR_DUPLICATES;
    }

    public void addWGS(WgsMetrics wgs) {
        this.MEAN_COVERAGE = wgs.MEAN_COVERAGE;
        this.PCT_10X = wgs.PCT_10X;
        this.PCT_30X = wgs.PCT_30X;
        this.PCT_100X = wgs.PCT_100X;
        this.GENOME_TERRITORY = wgs.GENOME_TERRITORY;
    }

    public void addQ(QMetric qMetric) {
        this.MSK_Q = qMetric.mskQ;
    }

    public void addRna(RnaSeqMetrics rnaSeqMetrics) {
        this.PCT_RIBOSOMAL_BASES = rnaSeqMetrics.PCT_RIBOSOMAL_BASES;
        this.PCT_CODING_BASES = rnaSeqMetrics.PCT_CODING_BASES;
        this.PCT_UTR_BASES = rnaSeqMetrics.PCT_UTR_BASES;
        this.PCT_INTRONIC_BASES = rnaSeqMetrics.PCT_INTRONIC_BASES;
        this.PCT_INTERGENIC_BASES = rnaSeqMetrics.PCT_INTERGENIC_BASES;
        this.PCT_MRNA_BASES = rnaSeqMetrics.PCT_MRNA_BASES;
    }

    public void addCpcg(CpcgMetrics cpcgMetrics) {
        this.G_REF_OXO_Q = cpcgMetrics.G_REF_OXO_Q;
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