package org.mskcc.cellranger.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Subset of 10x stats that are stored in the LIMS.
 */
@ToString @Getter @Setter @NoArgsConstructor
public class TenxLimsStats {
    private String IGO_SAMPLE_ID;
    private String OtherSampleId;
    private String RUN_ID;
    private String CELL_HASH_SAMPLE_ID;
    private Long AntibodyReadsPerCell;
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
}
