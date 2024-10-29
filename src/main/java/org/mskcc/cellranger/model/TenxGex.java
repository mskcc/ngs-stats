package org.mskcc.cellranger.model;

import javax.persistence.*;
import java.util.Date;
import lombok.Data;

@Entity
@Table(name = "10x_gex")
@Data
public class TenxGex {

    @Id
    @Column(name = "Sample_ID", nullable = false, length = 160)
    private String sampleId;

    @Column(name = "Run_ID", nullable = false, length = 50)
    private String runId;

    @Column(name = "CSV_Timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date csvTimestamp;

    @Column(name = "Estimated_Number_of_Cells")
    private Long estimatedNumberOfCells;

    @Column(name = "Mean_Reads_per_Cell")
    private Long meanReadsPerCell;

    @Column(name = "Median_Genes_per_Cell")
    private Long medianGenesPerCell;

    @Column(name = "Number_of_Reads")
    private Long numberOfReads;

    @Column(name = "Valid_Barcodes")
    private Double validBarcodes;

    @Column(name = "Sequencing_Saturation")
    private Double sequencingSaturation;

    @Column(name = "Q30_Bases_in_Barcode")
    private Double q30BasesInBarcode;

    @Column(name = "Q30_Bases_in_RNA_Read")
    private Double q30BasesInRnaRead;

    @Column(name = "Q30_Bases_in_UMI")
    private Double q30BasesInUmi;

    @Column(name = "Reads_Mapped_to_Genome")
    private Double readsMappedToGenome;

    @Column(name = "Reads_Mapped_Confidently_to_Genome")
    private Double readsMappedConfidentlyToGenome;

    @Column(name = "Reads_Mapped_Confidently_to_Intergenic_Regions")
    private Double readsMappedConfidentlyToIntergenicRegions;

    @Column(name = "Reads_Mapped_Confidently_to_Intronic_Regions")
    private Double readsMappedConfidentlyToIntronicRegions;

    @Column(name = "Reads_Mapped_Confidently_to_Exonic_Regions")
    private Double readsMappedConfidentlyToExonicRegions;

    @Column(name = "Reads_Mapped_Confidently_to_Transcriptome")
    private Double readsMappedConfidentlyToTranscriptome;

    @Column(name = "Reads_Mapped_Antisense_to_Gene")
    private Double readsMappedAntisenseToGene;

    @Column(name = "Fraction_Reads_in_Cells")
    private Double fractionReadsInCells;

    @Column(name = "Total_Genes_Detected")
    private Long totalGenesDetected;

    @Column(name = "Median_UMI_Counts_per_Cell")
    private Long medianUmiCountsPerCell;
}