package org.mskcc.cellranger.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;


@Entity
@IdClass(TenxAbcChId.class)
@Table(name = "10x_abc_ch")
@Data
public class TenxAbcCh implements Serializable {
    public static List<TenxLimsStats> toLimsStats(List<TenxAbcCh> inputList) {
        List<TenxLimsStats> list = new ArrayList<>();
        for (TenxAbcCh tenx : inputList) {
            TenxLimsStats lims = new TenxLimsStats();

            lims.setIGO_SAMPLE_ID(tenx.getSampleId());
            lims.setRUN_ID(tenx.getRunId());
            lims.setCELL_HASH_SAMPLE_ID(tenx.getChSampleId());
            lims.setCH_CELL_NUMBER(tenx.getChEstimatedNumberOfCellAssociatedBarcodes().doubleValue());
            lims.setABC_CH_FRACTION_UNRECOGNIZED(tenx.getChFractionUnrecognizedCmo());
            lims.setCH_MEAN_READS_PER_CELL(tenx.getChMeanReadsPerCellAssociatedBarcode());
            lims.setCH_MEAN_READS_PER_CELL(tenx.getChMeanReadsPerCellAssociatedBarcode());
            lims.setSEQUENCING_SATURATION(tenx.getAbcSequencingSaturation());
            lims.setCH_SEQUENCING_SATURATION(tenx.getChSequencingSaturation());
            lims.setTOTAL_READS(tenx.getAbcNumberOfReads());
            lims.setCH_TOTAL_READS(tenx.getChNumberOfReads());

            list.add(lims);
        }
        return list;
    }

    @Id
    @Column(name = "Sample_ID", nullable = false, length = 160)
    private String sampleId;

    @Id
    @Column(name = "CH_Sample_ID", nullable = false, length = 160)
    private String chSampleId;

    @Column(name = "Run_ID", nullable = false, length = 50)
    private String runId;

    @Column(name = "CSV_Timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date csvTimestamp;

    @Column(name = "ABC_Cells")
    private Long abcCells;

    @Column(name = "ABC_Antibody_reads_usable_per_cell")
    private Long abcAntibodyReadsUsablePerCell;

    @Column(name = "ABC_Median_UMI_counts_per_cell")
    private Long abcMedianUmiCountsPerCell;

    @Column(name = "GEX_Cells")
    private Long gexCells;

    @Column(name = "GEX_Median_UMI_counts_per_cell")
    private Long gexMedianUmiCountsPerCell;

    @Column(name = "GEX_Median_genes_per_cell")
    private Long gexMedianGenesPerCell;

    @Column(name = "GEX_Median_reads_per_cell")
    private Long gexMedianReadsPerCell;

    @Column(name = "GEX_Total_genes_detected")
    private Long gexTotalGenesDetected;

    @Column(name = "GEX_Cell_associated_barcodes_identified_as_multiplets", length = 128)
    private String gexCellAssociatedBarcodesIdentifiedAsMultiplets;

    @Column(name = "GEX_Cell_associated_barcodes_not_assigned_any_CMOs", length = 128)
    private String gexCellAssociatedBarcodesNotAssignedAnyCmos;

    @Column(name = "GEX_Cells_assigned_to_other_samples", length = 128)
    private String gexCellsAssignedToOtherSamples;

    @Column(name = "GEX_Cells_assigned_to_this_sample", length = 128)
    private String gexCellsAssignedToThisSample;

    @Column(name = "ABC_Estimated_number_of_cells")
    private Long abcEstimatedNumberOfCells;

    @Column(name = "ABC_Fraction_antibody_reads")
    private Double abcFractionAntibodyReads;

    @Column(name = "ABC_Fraction_antibody_reads_in_aggregate_barcodes")
    private Double abcFractionAntibodyReadsInAggregateBarcodes;

    @Column(name = "ABC_Fraction_antibody_reads_usable")
    private Double abcFractionAntibodyReadsUsable;

    @Column(name = "ABC_Means_reads_per_cell")
    private Long abcMeanReadsPerCell;

    @Column(name = "ABC_Number_of_reads")
    private Long abcNumberOfReads;

    @Column(name = "ABC_Sequencing_saturation")
    private Double abcSequencingSaturation;

    @Column(name = "ABC_Valid_UMIs")
    private Double abcValidUmis;

    @Column(name = "ABC_Valid_barcodes")
    private Double abcValidBarcodes;

    @Column(name = "GEX_Confidently_mapped_reads_in_cells")
    private Double gexConfidentlyMappedReadsInCells;

    @Column(name = "CH_Cell_associated_barcodes_identified_as_multiplets", length = 128)
    private String chCellAssociatedBarcodesIdentifiedAsMultiplets;

    @Column(name = "CH_Cell_associated_barcodes_not_assigned_any_CMOs", length = 128)
    private String chCellAssociatedBarcodesNotAssignedAnyCmos;

    @Column(name = "CH_Cells_assigned_to_a_sample", length = 128)
    private String chCellsAssignedToASample;

    @Column(name = "CH_Estimated_number_of_cell_associated_barcodes")
    private Long chEstimatedNumberOfCellAssociatedBarcodes;

    @Column(name = "CH_Fraction_CMO_reads")
    private Double chFractionCmoReads;

    @Column(name = "CH_Fractiom_CMO_reads_usable")
    private Double chFractionCmoReadsUsable;

    @Column(name = "CH_Fraction_reads_from_multiplets")
    private Double chFractionReadsFromMultiplets;

    @Column(name = "CH_Fraction_reads_in_cell_associated_barcodes")
    private Double chFractionReadsInCellAssociatedBarcodes;

    @Column(name = "CH_Fraction_unrecognized_CMO")
    private Double chFractionUnrecognizedCmo;

    @Column(name = "CH_Mean_reads_per_cell_associated_barcode")
    private Long chMeanReadsPerCellAssociatedBarcode;

    @Column(name = "CH_Median_CMO_UMIs_per_cell_associated_barcode")
    private Long chMedianCmoUmisPerCellAssociatedBarcode;

    @Column(name = "CH_Number_of_reads")
    private Long chNumberOfReads;

    @Column(name = "CH_Samples_assigned_at_least_one_cell")
    private Long chSamplesAssignedAtLeastOneCell;

    @Column(name = "CH_Sequencing_saturation")
    private Double chSequencingSaturation;

    @Column(name = "CH_Valid_UMIs")
    private Double chValidUmis;

    @Column(name = "CH_Valid_barcodes")
    private Double chValidBarcodes;

    @Column(name = "ABC_Number_of_short_reads_skipped")
    private Long abcNumberOfShortReadsSkipped;

    @Column(name = "GEX_Number_of_short_reads_skipped")
    private Long gexNumberOfShortReadsSkipped;

    @Column(name = "CH_Number_of_short_reads_skipped")
    private Long chNumberOfShortReadsSkipped;

    @Column(name = "CH_Singlet_capture_ratio")
    private Double chSingletCaptureRatio;
}

