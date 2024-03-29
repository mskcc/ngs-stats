package org.mskcc.picardstats.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * DTO https://martinfowler.com/eaaCatalog/dataTransferObject.html
 * for columns from multiple Picard files displayed together on the QC site.
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

    private String statsVersion;

    // ALIGNMENT SUMMARY METRICS
    public Long TOTAL_READS;
    public Long PF_READS_ALIGNED;
    public Long READS_ALIGNED_IN_PAIRS;
    public AlignmentSummaryMetrics.Category category;
    private Double PCT_ADAPTER;
    private Long unpairedReads; // derived field

    // Duplication Metrics
    private Long READ_PAIRS_EXAMINED;
    private Long READ_PAIRS_DUPLICATES;
    private Long UNMAPPED_READS;
    private Double PERCENT_DUPLICATION;

    // WGS
    private Long GENOME_TERRITORY;
    private Double MEAN_COVERAGE;
    private Double MEDIAN_COVERAGE;
    private Double PCT_EXC_MAPQ;
    private Double PCT_EXC_DUPE;
    private Double PCT_EXC_BASEQ;
    private Double PCT_EXC_TOTAL;
    private Double PCT_10X;
    private Double PCT_30X;
    private Double PCT_40X;
    private Double PCT_80X;
    private Double PCT_100X;

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

    //CPCG METRICS
    private Double G_REF_OXO_Q;

    public QCSiteStats() {}

    public QCSiteStats(PicardFile pf) {
        this.run = pf.getRun();
        this.request = pf.getRequest();
        this.sample = pf.getSample();
        this.referenceGenome = pf.getReferenceGenome();
        this.statsVersion = pf.getStatsVersion();
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
        this.GENOME_TERRITORY = wgs.GENOME_TERRITORY;
        this.MEAN_COVERAGE = wgs.MEAN_COVERAGE;
        this.MEDIAN_COVERAGE = wgs.MEDIAN_COVERAGE;
        this.PCT_EXC_MAPQ = wgs.PCT_EXC_MAPQ;
        this.PCT_EXC_DUPE = wgs.PCT_EXC_DUPE;
        this.PCT_EXC_BASEQ = wgs.PCT_EXC_BASEQ;
        this.PCT_EXC_TOTAL = wgs.PCT_EXC_TOTAL;
        this.PCT_10X = wgs.PCT_10X;
        this.PCT_30X = wgs.PCT_30X;
        this.PCT_40X = wgs.PCT_40X;
        this.PCT_80X = wgs.PCT_80X;
        this.PCT_100X = wgs.PCT_100X;
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