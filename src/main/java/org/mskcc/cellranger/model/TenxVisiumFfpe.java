package org.mskcc.cellranger.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;

@Entity
@Table(name = "10x_visium_ffpe")
@Data
public class TenxVisiumFfpe {
    public static List<TenxLimsStats> toLimsStats(List<TenxVisiumFfpe> inputList) {
        List<TenxLimsStats> list = new ArrayList<>();
        for (TenxVisiumFfpe tenx : inputList) {
            TenxLimsStats lims = new TenxLimsStats();
            lims.setSampleId(tenx.getSampleId());
            lims.setSequencerRunFolder(tenx.getRunId());

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

    @Column(name = "Number_of_Spots_Under_Tissue")
    private Long numberOfSpotsUnderTissue;

    @Column(name = "Number_of_Reads")
    private Long numberOfReads;

    @Column(name = "Mean_Reads_per_Spot")
    private Double meanReadsPerSpot;

    @Column(name = "Mean_Reads_Under_Tissue_per_Spot")
    private Double meanReadsUnderTissuePerSpot;

    @Column(name = "Fraction_of_Spots_Under_Tissue")
    private Double fractionOfSpotsUnderTissue;

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

    @Column(name = "Fraction_Reads_in_Spots_Under_Tissue")
    private Double fractionReadsInSpotsUnderTissue;

    @Column(name = "Median_Genes_per_Spot")
    private Double medianGenesPerSpot;

    @Column(name = "Median_UMI_Counts_per_Spot")
    private Double medianUmiCountsPerSpot;

    @Column(name = "Genes_Detected")
    private Long genesDetected;

    @Column(name = "Reads_Mapped_Confidently_to_the_Filtered_Probe_Set")
    private Double readsMappedConfidentlyToFilteredProbeSet;

    @Column(name = "Number_of_Genes")
    private Long numberOfGenes;

    @Column(name = "Number_of_Genes_GreaterThanEqualTo_10_UMIs")
    private Long numberOfGenesGte10Umis;

    @Column(name = "Reads_Half_Mapped_to_Probe_Set")
    private Double readsHalfMappedToProbeSet;

    @Column(name = "Reads_Split_Mapped_to_Probe_Set")
    private Double readsSplitMappedToProbeSet;

    @Column(name = "Estimated_UMIs_from_Genomic_DNA")
    private Double estimatedUmisFromGenomicDna;

    @Column(name = "Estimated_UMIs_from_Genomic_DNA_per_Unspliced_Probe")
    private Double estimatedUmisFromGenomicDnaPerUnsplicedProbe;
}