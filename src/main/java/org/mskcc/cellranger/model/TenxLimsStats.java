package org.mskcc.cellranger.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Subset of 10x stats that are stored in the LIMS.
 */
@ToString @NoArgsConstructor
public class TenxLimsStats {

    private String IGO_SAMPLE_ID;
    private String OtherSampleId;
    private String RUN_ID;
    private String CELL_HASH_SAMPLE_ID;
    private Long ANTIBODY_READS_PER_CELL;
    private Double CELL_NUMBER;
    private Double CH_CELL_NUMBER;
    private String CELLS_ASSIGNED_TO_SAMPLE;
    private Double ABC_CH_FRACTION_UNRECOGNIZED;
    private Double MEAN_READS_PER_CELL;
    private Long CH_MEAN_READS_PER_CELL;
    private Double ATAC_MEAN_RAW_READS_PER_CELL;
    private Double MEAN_READS_PER_SPOT;
    private Long MEDIAN_CH_UMIs_PER_CELL_BARCODE;
    private Double MEDIAN_GENES_PER_SPOT;
    private Double MEDIAN_GENES_OR_FRAGMENTS_PER_CELL;
    private Double ATAC_MEDIAN_HIGH_QUALITY_FRAGMENTS_PER_CELL; // ATAC_MEDIAN_HIGH_QUALITY_FRAGMENTS_PER_CELL
    private Double MEDIAN_IGL_UMIs_PER_CELL;  // MEDIAN_IGL_UMIs_PER_CELL
    private Double MEDIAN_TRA_IGH_UMIs_PER_CELL; // MEDIAN_TRA_IGH_UMIs_PER_CELL
    private Double MEDIAN_TRB_IGK_UMIs_PER_CELL; // MEDIAN_TRB_IGK_UMIs_PER_CELL
    private Double READS_MAPPED_CONFIDENTLY_TO_GENOME;
    private Double ATAC_CONFIDENTLY_MAPPED_READ_PAIRS;
    private Double READS_MAPPED_TO_TRANSCRIPTOME;
    private Double VDJ_READS_MAPPED;
    private Double READS_MAPPED_CONFIDENTLY_TO_PROBE_SET; // READS_MAPPED_CONFIDENTLY_TO_PROBE_SET
    private Double SAMPLES_ASSIGNED_AT_LEAST_ONE_CELL;
    private Double SEQUENCING_SATURATION;
    private Double CH_SEQUENCING_SATURATION;
    private Long TOTAL_READS;
    private Long CH_TOTAL_READS;
    private Long ATAC_TOTAL_READS;

    public String getIGO_SAMPLE_ID() {
        return IGO_SAMPLE_ID;
    }

    public void setIGO_SAMPLE_ID(String IGO_SAMPLE_ID) {
        this.IGO_SAMPLE_ID = IGO_SAMPLE_ID;
    }

    public String getOtherSampleId() {
        return OtherSampleId;
    }

    public void setOtherSampleId(String otherSampleId) {
        OtherSampleId = otherSampleId;
    }

    public String getRUN_ID() {
        return RUN_ID;
    }

    public void setRUN_ID(String RUN_ID) {
        this.RUN_ID = RUN_ID;
    }

    public String getCELL_HASH_SAMPLE_ID() {
        return CELL_HASH_SAMPLE_ID;
    }

    public void setCELL_HASH_SAMPLE_ID(String CELL_HASH_SAMPLE_ID) {
        this.CELL_HASH_SAMPLE_ID = CELL_HASH_SAMPLE_ID;
    }

    public Long getANTIBODY_READS_PER_CELL() {
        return ANTIBODY_READS_PER_CELL;
    }

    public void setANTIBODY_READS_PER_CELL(Long ANTIBODY_READS_PER_CELL) {
        this.ANTIBODY_READS_PER_CELL = ANTIBODY_READS_PER_CELL;
    }

    public Double getCELL_NUMBER() {
        return CELL_NUMBER;
    }

    public void setCELL_NUMBER(Double CELL_NUMBER) {
        this.CELL_NUMBER = CELL_NUMBER;
    }

    public Double getCH_CELL_NUMBER() {
        return CH_CELL_NUMBER;
    }

    public void setCH_CELL_NUMBER(Double CH_CELL_NUMBER) {
        this.CH_CELL_NUMBER = CH_CELL_NUMBER;
    }

    public String getCELLS_ASSIGNED_TO_SAMPLE() {
        return CELLS_ASSIGNED_TO_SAMPLE;
    }

    public void setCELLS_ASSIGNED_TO_SAMPLE(String CELLS_ASSIGNED_TO_SAMPLE) {
        this.CELLS_ASSIGNED_TO_SAMPLE = CELLS_ASSIGNED_TO_SAMPLE;
    }

    public Double getABC_CH_FRACTION_UNRECOGNIZED() {
        return ABC_CH_FRACTION_UNRECOGNIZED;
    }

    public void setABC_CH_FRACTION_UNRECOGNIZED(Double ABC_CH_FRACTION_UNRECOGNIZED) {
        this.ABC_CH_FRACTION_UNRECOGNIZED = ABC_CH_FRACTION_UNRECOGNIZED;
    }

    public Double getMEAN_READS_PER_CELL() {
        return MEAN_READS_PER_CELL;
    }

    public void setMEAN_READS_PER_CELL(Double MEAN_READS_PER_CELL) {
        this.MEAN_READS_PER_CELL = MEAN_READS_PER_CELL;
    }

    public Long getCH_MEAN_READS_PER_CELL() {
        return CH_MEAN_READS_PER_CELL;
    }

    public void setCH_MEAN_READS_PER_CELL(Long CH_MEAN_READS_PER_CELL) {
        this.CH_MEAN_READS_PER_CELL = CH_MEAN_READS_PER_CELL;
    }

    public Double getATAC_MEAN_RAW_READS_PER_CELL() {
        return ATAC_MEAN_RAW_READS_PER_CELL;
    }

    public void setATAC_MEAN_RAW_READS_PER_CELL(Double ATAC_MEAN_RAW_READS_PER_CELL) {
        this.ATAC_MEAN_RAW_READS_PER_CELL = ATAC_MEAN_RAW_READS_PER_CELL;
    }

    public Double getMEAN_READS_PER_SPOT() {
        return MEAN_READS_PER_SPOT;
    }

    public void setMEAN_READS_PER_SPOT(Double MEAN_READS_PER_SPOT) {
        this.MEAN_READS_PER_SPOT = MEAN_READS_PER_SPOT;
    }

    public Long getMEDIAN_CH_UMIs_PER_CELL_BARCODE() {
        return MEDIAN_CH_UMIs_PER_CELL_BARCODE;
    }

    public void setMEDIAN_CH_UMIs_PER_CELL_BARCODE(Long MEDIAN_CH_UMIs_PER_CELL_BARCODE) {
        this.MEDIAN_CH_UMIs_PER_CELL_BARCODE = MEDIAN_CH_UMIs_PER_CELL_BARCODE;
    }

    public Double getMEDIAN_GENES_PER_SPOT() {
        return MEDIAN_GENES_PER_SPOT;
    }

    public void setMEDIAN_GENES_PER_SPOT(Double MEDIAN_GENES_PER_SPOT) {
        this.MEDIAN_GENES_PER_SPOT = MEDIAN_GENES_PER_SPOT;
    }

    public Double getMEDIAN_GENES_OR_FRAGMENTS_PER_CELL() {
        return MEDIAN_GENES_OR_FRAGMENTS_PER_CELL;
    }

    public void setMEDIAN_GENES_OR_FRAGMENTS_PER_CELL(Double MEDIAN_GENES_OR_FRAGMENTS_PER_CELL) {
        this.MEDIAN_GENES_OR_FRAGMENTS_PER_CELL = MEDIAN_GENES_OR_FRAGMENTS_PER_CELL;
    }

    public Double getATAC_MEDIAN_HIGH_QUALITY_FRAGMENTS_PER_CELL() {
        return ATAC_MEDIAN_HIGH_QUALITY_FRAGMENTS_PER_CELL;
    }

    public void setATAC_MEDIAN_HIGH_QUALITY_FRAGMENTS_PER_CELL(Double ATAC_MEDIAN_HIGH_QUALITY_FRAGMENTS_PER_CELL) {
        this.ATAC_MEDIAN_HIGH_QUALITY_FRAGMENTS_PER_CELL = ATAC_MEDIAN_HIGH_QUALITY_FRAGMENTS_PER_CELL;
    }

    public Double getMEDIAN_IGL_UMIs_PER_CELL() {
        return MEDIAN_IGL_UMIs_PER_CELL;
    }

    public void setMEDIAN_IGL_UMIs_PER_CELL(Double MEDIAN_IGL_UMIs_PER_CELL) {
        this.MEDIAN_IGL_UMIs_PER_CELL = MEDIAN_IGL_UMIs_PER_CELL;
    }

    public Double getMEDIAN_TRA_IGH_UMIs_PER_CELL() {
        return MEDIAN_TRA_IGH_UMIs_PER_CELL;
    }

    public void setMEDIAN_TRA_IGH_UMIs_PER_CELL(Double MEDIAN_TRA_IGH_UMIs_PER_CELL) {
        this.MEDIAN_TRA_IGH_UMIs_PER_CELL = MEDIAN_TRA_IGH_UMIs_PER_CELL;
    }

    public Double getMEDIAN_TRB_IGK_UMIs_PER_CELL() {
        return MEDIAN_TRB_IGK_UMIs_PER_CELL;
    }

    public void setMEDIAN_TRB_IGK_UMIs_PER_CELL(Double MEDIAN_TRB_IGK_UMIs_PER_CELL) {
        this.MEDIAN_TRB_IGK_UMIs_PER_CELL = MEDIAN_TRB_IGK_UMIs_PER_CELL;
    }

    public Double getREADS_MAPPED_CONFIDENTLY_TO_GENOME() {
        return READS_MAPPED_CONFIDENTLY_TO_GENOME;
    }

    public void setREADS_MAPPED_CONFIDENTLY_TO_GENOME(Double READS_MAPPED_CONFIDENTLY_TO_GENOME) {
        this.READS_MAPPED_CONFIDENTLY_TO_GENOME = READS_MAPPED_CONFIDENTLY_TO_GENOME;
    }

    public Double getATAC_CONFIDENTLY_MAPPED_READ_PAIRS() {
        return ATAC_CONFIDENTLY_MAPPED_READ_PAIRS;
    }

    public void setATAC_CONFIDENTLY_MAPPED_READ_PAIRS(Double ATAC_CONFIDENTLY_MAPPED_READ_PAIRS) {
        this.ATAC_CONFIDENTLY_MAPPED_READ_PAIRS = ATAC_CONFIDENTLY_MAPPED_READ_PAIRS;
    }

    public Double getREADS_MAPPED_TO_TRANSCRIPTOME() {
        return READS_MAPPED_TO_TRANSCRIPTOME;
    }

    public void setREADS_MAPPED_TO_TRANSCRIPTOME(Double READS_MAPPED_TO_TRANSCRIPTOME) {
        this.READS_MAPPED_TO_TRANSCRIPTOME = READS_MAPPED_TO_TRANSCRIPTOME;
    }

    public Double getVDJ_READS_MAPPED() {
        return VDJ_READS_MAPPED;
    }

    public void setVDJ_READS_MAPPED(Double VDJ_READS_MAPPED) {
        this.VDJ_READS_MAPPED = VDJ_READS_MAPPED;
    }

    public Double getREADS_MAPPED_CONFIDENTLY_TO_PROBE_SET() {
        return READS_MAPPED_CONFIDENTLY_TO_PROBE_SET;
    }

    public void setREADS_MAPPED_CONFIDENTLY_TO_PROBE_SET(Double READS_MAPPED_CONFIDENTLY_TO_PROBE_SET) {
        this.READS_MAPPED_CONFIDENTLY_TO_PROBE_SET = READS_MAPPED_CONFIDENTLY_TO_PROBE_SET;
    }

    public Double getSAMPLES_ASSIGNED_AT_LEAST_ONE_CELL() {
        return SAMPLES_ASSIGNED_AT_LEAST_ONE_CELL;
    }

    public void setSAMPLES_ASSIGNED_AT_LEAST_ONE_CELL(Double SAMPLES_ASSIGNED_AT_LEAST_ONE_CELL) {
        this.SAMPLES_ASSIGNED_AT_LEAST_ONE_CELL = SAMPLES_ASSIGNED_AT_LEAST_ONE_CELL;
    }

    public Double getSEQUENCING_SATURATION() {
        return SEQUENCING_SATURATION;
    }

    public void setSEQUENCING_SATURATION(Double SEQUENCING_SATURATION) {
        this.SEQUENCING_SATURATION = SEQUENCING_SATURATION;
    }

    public Double getCH_SEQUENCING_SATURATION() {
        return CH_SEQUENCING_SATURATION;
    }

    public void setCH_SEQUENCING_SATURATION(Double CH_SEQUENCING_SATURATION) {
        this.CH_SEQUENCING_SATURATION = CH_SEQUENCING_SATURATION;
    }

    public Long getTOTAL_READS() {
        return TOTAL_READS;
    }

    public void setTOTAL_READS(Long TOTAL_READS) {
        this.TOTAL_READS = TOTAL_READS;
    }

    public Long getCH_TOTAL_READS() {
        return CH_TOTAL_READS;
    }

    public void setCH_TOTAL_READS(Long CH_TOTAL_READS) {
        this.CH_TOTAL_READS = CH_TOTAL_READS;
    }

    public Long getATAC_TOTAL_READS() {
        return ATAC_TOTAL_READS;
    }

    public void setATAC_TOTAL_READS(Long ATAC_TOTAL_READS) {
        this.ATAC_TOTAL_READS = ATAC_TOTAL_READS;
    }
}
