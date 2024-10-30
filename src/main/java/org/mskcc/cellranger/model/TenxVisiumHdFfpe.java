package org.mskcc.cellranger.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;

@Entity
@Table(name = "10x_visium_hd_ffpe")
@Data
public class TenxVisiumHdFfpe {
    public static List<TenxLimsStats> toLimsStats(List<TenxVisiumHdFfpe> inputList) {
        List<TenxLimsStats> list = new ArrayList<>();
        for (TenxVisiumHdFfpe tenx : inputList) {
            TenxLimsStats lims = new TenxLimsStats();

            lims.setSampleId(tenx.getSampleId());
            lims.setSequencerRunFolder(tenx.getRunId());
            lims.setSampleId(tenx.getSampleId());
            lims.setSequencerRunFolder(tenx.getRunId());
            lims.setReadMappedConfidentlyToProbSet(tenx.getReadsMappedConfidentlyToProbeSet());
            lims.setSeqSaturation(tenx.getSequencingSaturation());
            lims.setTotalReads(tenx.getNumberOfReads());

            list.add(lims);
        }
        return list;
    }

    @Id
    @Column(name = "Sample_ID", nullable = false, length = 160)
    private String sampleId;

    @Column(name = "Run_ID", nullable = false, length = 50)
    private String runId;

    @Column(name = "CSV_Timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date csvTimestamp;

    @Column(name = "Number_of_Reads")
    private Long numberOfReads;

    @Column(name = "Valid_Barcodes")
    private Double validBarcodes;

    @Column(name = "Valid_UMIs")
    private Double validUmis;

    @Column(name = "Sequencing_Saturation")
    private Double sequencingSaturation;

    @Column(name = "Q30_Bases_in_Barcode")
    private Double q30BasesInBarcode;

    @Column(name = "Q30_Bases_in_Probe_Read")
    private Double q30BasesInProbeRead;

    @Column(name = "Q30_Bases_in_UMI")
    private Double q30BasesInUmi;

    @Column(name = "Reads_Mapped_to_Probe_Set")
    private Double readsMappedToProbeSet;

    @Column(name = "Reads_Mapped_Confidently_to_Probe_Set")
    private Double readsMappedConfidentlyToProbeSet;

    @Column(name = "Fraction_Reads_in_Squares_Under_Tissue")
    private Double fractionReadsInSquaresUnderTissue;

    @Column(name = "Genes_Detected")
    private Long genesDetected;

    @Column(name = "Reads_Mapped_Confidently_to_the_Filtered_Probe_Set")
    private Double readsMappedConfidentlyToFilteredProbeSet;

    @Column(name = "Number_of_Genes")
    private Long numberOfGenes;

    @Column(name = "Reads_Half_Mapped_to_Probe_Set")
    private Double readsHalfMappedToProbeSet;

    @Column(name = "Reads_Split_Mapped_to_Probe_Set")
    private Double readsSplitMappedToProbeSet;

    @Column(name = "Estimated_UMIs_from_Genomic_DNA")
    private Double estimatedUmisFromGenomicDna;

    @Column(name = "Estimated_UMIs_from_Genomic_DNA_per_Unspliced_Probe")
    private Double estimatedUmisFromGenomicDnaPerUnsplicedProbe;

    @Column(name = "Number_of_Squares_Under_Tissue_2")
    private Double numberOfSquaresUnderTissue2;

    @Column(name = "Mean_Reads_Under_Tissue_per_Square_2")
    private Double meanReadsUnderTissuePerSquare2;

    @Column(name = "Fraction_of_Squares_Under_Tissue_2")
    private Double fractionOfSquaresUnderTissue2;

    @Column(name = "Mean_Genes_Under_Tissue_per_Square_2")
    private Double meanGenesUnderTissuePerSquare2;

    @Column(name = "Mean_UMIs_Under_Tissue_per_Square_2")
    private Double meanUmisUnderTissuePerSquare2;

    @Column(name = "Total_Genes_Detected_Under_Tissue_2")
    private Long totalGenesDetectedUnderTissue2;

    @Column(name = "Number_of_Bins_Under_Tissue_8")
    private Long numberOfBinsUnderTissue8;

    @Column(name = "Mean_Reads_Under_Tissue_per_Bin_8")
    private Double meanReadsUnderTissuePerBin8;

    @Column(name = "Fraction_of_Bins_Under_Tissue_8")
    private Double fractionOfBinsUnderTissue8;

    @Column(name = "Mean_Genes_Under_Tissue_per_Bin_8")
    private Double meanGenesUnderTissuePerBin8;

    @Column(name = "Mean_UMIs_Under_Tissue_per_Bin_8")
    private Double meanUmisUnderTissuePerBin8;

    @Column(name = "Total_Genes_Detected_Under_Tissue_8")
    private Long totalGenesDetectedUnderTissue8;

    @Column(name = "Number_of_Bins_Under_Tissue_16")
    private Long numberOfBinsUnderTissue16;

    @Column(name = "Mean_Reads_Under_Tissue_per_Bin_16")
    private Double meanReadsUnderTissuePerBin16;

    @Column(name = "Fraction_of_Bins_Under_Tissue_16")
    private Double fractionOfBinsUnderTissue16;

    @Column(name = "Mean_Genes_Under_Tissue_per_Bin_16")
    private Double meanGenesUnderTissuePerBin16;

    @Column(name = "Mean_UMIs_Under_Tissue_per_Bin_16")
    private Double meanUmisUnderTissuePerBin16;

    @Column(name = "Total_Genes_Detected_Under_Tissue_16")
    private Long totalGenesDetectedUnderTissue16;
}