package org.mskcc.cellranger.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Subset of 10x stats that are stored in the LIMS.
 */
@ToString @Getter @Setter @NoArgsConstructor
public class TenxLimsStats {
    private String SampleId;
    private String OtherSampleId;
    private String SequencerRunFolder;
    private Long AntibodyReadsPerCell;
    private Double CellNumber;
    private Double ChCellNumber;
    private String CellsAssignedToSample;
    private Double FractionUnrecognized;
    private Double MeanReadsPerCell;
    private Integer ChMeanReadsPerCell;
    private Double AtacMeanRawReadsPerCell;
    private Double MeanReadsPerSpot;
    private Integer MedianUmisPerCellBarcode;
    private Double MedianGenesOrFragmentsPerCell;
    private Double AtacMedianHighQultyFragPerCell; // ATAC_MEDIAN_HIGH_QUALITY_FRAGMENTS_PER_CELL
    private Double MedianIGLUmisPerCell;  // MEDIAN_IGL_UMIs_PER_CELL
    private Double MedianTraIghUmisPerCell; // MEDIAN_TRA_IGH_UMIs_PER_CELL
    private Double MedianTrbIgkUmisPerCell; // MEDIAN_TRB_IGK_UMIs_PER_CELL
    private Double ReadsMappedConfidentlyToGenome;
    private Double AtacConfidentlyMappedReadsPair;
    private Double ReadsMappedToTranscriptome;
    private Double VdjReadsMapped;
    private Double ReadMappedConfidentlyToProbSet; // READS_MAPPED_CONFIDENTLY_TO_PROBE_SET
    private Double SamplesAssignedAtLeastOneCell;
    private Double SeqSaturation;
    private Double ChSeqSaturation;
    private Integer TotalReads;
    private Integer ChTotalReads;
    private Integer AtacTotalReads;
}
