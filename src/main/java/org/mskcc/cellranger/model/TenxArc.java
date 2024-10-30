package org.mskcc.cellranger.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;

@Entity
@Table(name = "10x_arc")
@Data
public class TenxArc {
    public static List<TenxLimsStats> toLimsStats(List<TenxArc> inputList) {
        List<TenxLimsStats> list = new ArrayList<>();
        for (TenxArc tenx : inputList) {
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

    @Column(name = "Genome", length = 128)
    private String genome;

    @Column(name = "Pipeline_version", length = 128)
    private String pipelineVersion;

    @Column(name = "Estimated_number_of_cells")
    private Long estimatedNumberOfCells;

    @Column(name = "Feature_linkages_detected")
    private Long featureLinkagesDetected;

    @Column(name = "Linked_genes")
    private Long linkedGenes;

    @Column(name = "Linked_peaks")
    private Long linkedPeaks;

    @Column(name = "ATAC_Confidently_mapped_read_pairs")
    private Double atacConfidentlyMappedReadPairs;

    @Column(name = "ATAC_Fraction_of_genome_in_peaks")
    private Double atacFractionOfGenomeInPeaks;

    @Column(name = "ATAC_Fraction_of_high_quality_fragments_in_cells")
    private Double atacFractionOfHighQualityFragmentsInCells;

    @Column(name = "ATAC_Fraction_of_high_quality_fragments_in_overlapping_TSS")
    private Double atacFractionOfHighQualityFragmentsInOverlappingTss;

    @Column(name = "ATAC_Fraction_of_high_quality_Fragments_in_overlapping_peaks")
    private Double atacFractionOfHighQualityFragmentsInOverlappingPeaks;

    @Column(name = "ATAC_Fraction_of_transposition_events_in_peaks_in_cells")
    private Double atacFractionOfTranspositionEventsInPeaksInCells;

    @Column(name = "ATAC_Mean_raw_read_pairs_per_cell")
    private Double atacMeanRawReadPairsPerCell;

    @Column(name = "ATAC_Median_high_quality_fragments_per_cell")
    private Double atacMedianHighQualityFragmentsPerCell;

    @Column(name = "ATAC_Non_nuclear_read_pairs")
    private Double atacNonNuclearReadPairs;

    @Column(name = "ATAC_Number_of_peaks")
    private Long atacNumberOfPeaks;

    @Column(name = "ATAC_Percent_duplicates")
    private Double atacPercentDuplicates;

    @Column(name = "ATAC_Q30_bases_in_barcode")
    private Double atacQ30BasesInBarcode;

    @Column(name = "ATAC_Q30_bases_in_read_1")
    private Double atacQ30BasesInRead1;

    @Column(name = "ATAC_Q30_bases_in_read_2")
    private Double atacQ30BasesInRead2;

    @Column(name = "ATAC_Q30_bases_in_sample_index_i1")
    private Double atacQ30BasesInSampleIndexI1;

    @Column(name = "ATAC_Sequenced_read_pairs")
    private Long atacSequencedReadPairs;

    @Column(name = "ATAC_TSS_enrichment_score")
    private Double atacTssEnrichmentScore;

    @Column(name = "ATAC_Unmapped_read_pairs")
    private Double atacUnmappedReadPairs;

    @Column(name = "ATAC_Valid_barcodes")
    private Double atacValidBarcodes;

    @Column(name = "GEX_Fraction_of_transcriptomic_reads_in_cells")
    private Double gexFractionOfTranscriptomicReadsInCells;

    @Column(name = "GEX_Mean_raw_reads_per_cell")
    private Double gexMeanRawReadsPerCell;

    @Column(name = "GEX_Median_UMI_counts_per_cell")
    private Double gexMedianUmiCountsPerCell;

    @Column(name = "GEX_Median_genes_per_cell")
    private Double gexMedianGenesPerCell;

    @Column(name = "GEX_Percent_duplicates")
    private Double gexPercentDuplicates;

    @Column(name = "GEX_Q30_bases_in_UMI")
    private Double gexQ30BasesInUmi;

    @Column(name = "GEX_Q30_bases_in_barcode")
    private Double gexQ30BasesInBarcode;

    @Column(name = "GEX_Q30_bases_in_read_2")
    private Double gexQ30BasesInRead2;

    @Column(name = "GEX_Q30_bases_in_sample_index_i1")
    private Double gexQ30BasesInSampleIndexI1;

    @Column(name = "GEX_Q30_bases_in_sample_index_i2")
    private Double gexQ30BasesInSampleIndexI2;

    @Column(name = "GEX_Reads_mapped_antisense_to_gene")
    private Double gexReadsMappedAntisenseToGene;

    @Column(name = "GEX_Reads_mapped_confidently_to_exonic_regions")
    private Double gexReadsMappedConfidentlyToExonicRegions;

    @Column(name = "GEX_Reads_mapped_confidently_to_genome")
    private Double gexReadsMappedConfidentlyToGenome;

    @Column(name = "GEX_Reads_mapped_confidently_to_intergenic_regions")
    private Double gexReadsMappedConfidentlyToIntergenicRegions;

    @Column(name = "GEX_Reads_mapped_confidently_to_intronic_regions")
    private Double gexReadsMappedConfidentlyToIntronicRegions;

    @Column(name = "GEX_Reads_mapped_confidently_to_transcriptome")
    private Double gexReadsMappedConfidentlyToTranscriptome;

    @Column(name = "GEX_Reads_mapped_to_genome")
    private Double gexReadsMappedToGenome;

    @Column(name = "GEX_Reads_with_TSO")
    private Double gexReadsWithTso;

    @Column(name = "GEX_Sequenced_read_pairs")
    private Long gexSequencedReadPairs;

    @Column(name = "GEX_Total_genes_detected")
    private Long gexTotalGenesDetected;

    @Column(name = "GEX_Valid_UMIs")
    private Double gexValidUmis;

    @Column(name = "GEX_Valid_barcodes")
    private Double gexValidBarcodes;
}

