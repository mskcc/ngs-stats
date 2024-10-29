package org.mskcc.cellranger.model;

import javax.persistence.*;
import java.util.Date;
import lombok.Data;

@Entity
@Table(name = "10x_ch")
@Data
public class TenxCh {

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

    @Column(name = "GEX_Cells_assigned_to_other_samples", length = 128)
    private String gexCellsAssignedToOtherSamples;

    @Column(name = "GEX_Cells_assigned_to_this_sample", length = 128)
    private String gexCellsAssignedToThisSample;

    @Column(name = "Cell_associated_barcodes_identified_as_multiplets", length = 128)
    private String cellAssociatedBarcodesIdentifiedAsMultiplets;

    @Column(name = "Cell_associated_barcodes_not_assigned_any_CMOs", length = 128)
    private String cellAssociatedBarcodesNotAssignedAnyCMOs;

    @Column(name = "Cells_assigned_to_a_sample", length = 128)
    private String cellsAssignedToASample;

    @Column(name = "Estimated_number_of_cell_associated_barcodes")
    private Double estimatedNumberOfCellAssociatedBarcodes;

    @Column(name = "Fraction_CMO_reads")
    private Double fractionCmoReads;

    @Column(name = "Fraction_CMO_reads_usable")
    private Double fractionCmoReadsUsable;

    @Column(name = "Fraction_reads_from_multiplets")
    private Double fractionReadsFromMultiplets;

    @Column(name = "Fraction_reads_in_cell_associated_barcodes")
    private Double fractionReadsInCellAssociatedBarcodes;

    @Column(name = "Fraction_unrecognized_CMO")
    private Double fractionUnrecognizedCmo;

    @Column(name = "Mean_reads_per_cell_associated_barcode")
    private Long meanReadsPerCellAssociatedBarcode;

    @Column(name = "Median_CMO_UMIs_per_cell_associated_barcode")
    private Long medianCmoUmisPerCellAssociatedBarcode;

    @Column(name = "Number_of_reads")
    private Long numberOfReads;

    @Column(name = "Samples_assigned_at_least_one_cell")
    private Long samplesAssignedAtLeastOneCell;

    @Column(name = "Sequencing_saturation")
    private Double sequencingSaturation;

    @Column(name = "Valid_UMIs")
    private Double validUmis;

    @Column(name = "Valid_barcodes")
    private Double validBarcodes;
}
