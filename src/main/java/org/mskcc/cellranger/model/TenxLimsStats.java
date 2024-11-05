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
    @JsonProperty("IGO_SAMPLE_ID")
    private String SampleId;
    private String OtherSampleId;
    @JsonProperty("RUN_ID")
    private String SequencerRunFolder;
    @JsonProperty("CELL_HASH_SAMPLE_ID")
    private String CellHashSampleId;
    @JsonProperty("ANTIBODY_READS_PER_CELL")
    private Long AntibodyReadsPerCell;
    @JsonProperty("CELL_NUMBER")
    private Double CellNumber;
    @JsonProperty("CH_CELL_NUMBER")
    private Double ChCellNumber;
    @JsonProperty("CELLS_ASSIGNED_TO_SAMPLE")
    private String CellsAssignedToSample;
    @JsonProperty("ABC_CH_FRACTION_UNRECOGNIZED")
    private Double FractionUnrecognized;
    @JsonProperty("MEAN_READS_PER_CELL")
    private Double MeanReadsPerCell;
    @JsonProperty("CH_MEAN_READS_PER_CELL")
    private Long ChMeanReadsPerCell;
    @JsonProperty("ATAC_MEAN_RAW_READS_PER_CELL")
    private Double AtacMeanRawReadsPerCell;
    @JsonProperty("MEAN_READS_PER_SPOT")
    private Double MeanReadsPerSpot;
    @JsonProperty("MEDIAN_CH_UMIs_PER_CELL_BARCODE")
    private Long MedianUmisPerCellBarcode;
    @JsonProperty("MEDIAN_GENES_PER_SPOT")
    private Double MedianGenesPerSpot;
    @JsonProperty("MEDIAN_GENES_OR_FRAGMENTS_PER_CELL")
    private Double MedianGenesOrFragmentsPerCell;
    @JsonProperty("ATAC_MEDIAN_HIGH_QUALITY_FRAGMENTS_PER_CELL")
    private Double AtacMedianHighQultyFragPerCell; // ATAC_MEDIAN_HIGH_QUALITY_FRAGMENTS_PER_CELL
    @JsonProperty("MEDIAN_IGL_UMIs_PER_CELL")
    private Double MedianIGLUmisPerCell;  // MEDIAN_IGL_UMIs_PER_CELL
    @JsonProperty("MEDIAN_TRA_IGH_UMIs_PER_CELL")
    private Double MedianTraIghUmisPerCell; // MEDIAN_TRA_IGH_UMIs_PER_CELL
    @JsonProperty("MEDIAN_TRB_IGK_UMIs_PER_CELL")
    private Double MedianTrbIgkUmisPerCell; // MEDIAN_TRB_IGK_UMIs_PER_CELL
    @JsonProperty("MEDIAN_GENES_OR_FRAGMENTS_PER_CELL")
    private Double ReadsMappedConfidentlyToGenome;
    @JsonProperty("ATAC_CONFIDENTLY_MAPPED_READ_PAIRS")
    private Double AtacConfidentlyMappedReadsPair;
    @JsonProperty("READS_MAPPED_TO_TRANSCRIPTOME")
    private Double ReadsMappedToTranscriptome;
    @JsonProperty("VDJ_READS_MAPPED")
    private Double VdjReadsMapped;
    @JsonProperty("READS_MAPPED_CONFIDENTLY_TO_PROBE_SET")
    private Double ReadMappedConfidentlyToProbSet; // READS_MAPPED_CONFIDENTLY_TO_PROBE_SET
    @JsonProperty("SAMPLES_ASSIGNED_AT_LEAST_ONE_CELL")
    private Double SamplesAssignedAtLeastOneCell;
    @JsonProperty("SEQUENCING_SATURATION")
    private Double SeqSaturation;
    @JsonProperty("CH_SEQUENCING_SATURATION")
    private Double ChSeqSaturation;
    @JsonProperty("TOTAL_READS")
    private Long TotalReads;
    @JsonProperty("CH_TOTAL_READS")
    private Long ChTotalReads;
    @JsonProperty("ATAC_TOTAL_READS")
    private Long AtacTotalReads;
}
