package org.mskcc.cellranger.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;

@Entity
@Table(name = "10x_atac")
@Data
public class TenxAtac {
    public static List<TenxLimsStats> toLimsStats(List<TenxAtac> inputList) {
        List<TenxLimsStats> list = new ArrayList<>();
        for (TenxAtac tenx : inputList) {
            TenxLimsStats lims = new TenxLimsStats();

            lims.setIGO_SAMPLE_ID(tenx.getSampleId());
            lims.setRUN_ID(tenx.getRunId());
            lims.setATAC_MEAN_RAW_READS_PER_CELL(tenx.getMeanRawReadPairsPerCell());
            lims.setATAC_MEDIAN_HIGH_QUALITY_FRAGMENTS_PER_CELL(tenx.getMedianHighQualityFragmentsPerCell());
            lims.setATAC_CONFIDENTLY_MAPPED_READ_PAIRS(tenx.getConfidentlyMappedReadPairs());
            lims.setATAC_TOTAL_READS(tenx.getSequencedReadPairs());

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

    @Column(name = "Confidently_mapped_read_pairs")
    private Double confidentlyMappedReadPairs;

    @Column(name = "Estimated_bulk_library_complexity")
    private Double estimatedBulkLibraryComplexity;

    @Column(name = "Fraction_of_all_fragments_in_cells")
    private Double fractionOfAllFragmentsInCells;

    @Column(name = "Fraction_all_fragments_pass_all_filters_overlap_called_peaks")
    private Double fractionAllFragmentsPassAllFiltersOverlapCalledPeaks;

    @Column(name = "Fraction_of_genome_in_peaks")
    private Double fractionOfGenomeInPeaks;

    @Column(name = "Fraction_of_high_quality_fragments_in_cells")
    private Double fractionOfHighQualityFragmentsInCells;

    @Column(name = "Fraction_of_high_quality_fragments_overlapping_TSS")
    private Double fractionOfHighQualityFragmentsOverlappingTss;

    @Column(name = "Fraction_of_high_quality_fragments_overlapping_peaks")
    private Double fractionOfHighQualityFragmentsOverlappingPeaks;

    @Column(name = "Fraction_of_transposition_events_in_peaks_in_cells")
    private Double fractionOfTranspositionEventsInPeaksInCells;

    @Column(name = "Fragments_flanking_a_single_nucleosome")
    private Double fragmentsFlankingASingleNucleosome;

    @Column(name = "Fragments_in_nucleosome_free_regions")
    private Double fragmentsInNucleosomeFreeRegions;

    @Column(name = "Mean_raw_read_pairs_per_cell")
    private Double meanRawReadPairsPerCell;

    @Column(name = "Median_high_quality_fragments_per_cell")
    private Double medianHighQualityFragmentsPerCell;

    @Column(name = "Non_nuclear_read_pairs")
    private Double nonNuclearReadPairs;

    @Column(name = "Number_of_peaks")
    private Long numberOfPeaks;

    @Column(name = "Percent_duplicates")
    private Double percentDuplicates;

    @Column(name = "Q30_bases_in_barcode")
    private Double q30BasesInBarcode;

    @Column(name = "Q30_bases_in_read_1")
    private Double q30BasesInRead1;

    @Column(name = "Q30_bases_in_read_2")
    private Double q30BasesInRead2;

    @Column(name = "Q30_bases_in_sample_index_i1")
    private Double q30BasesInSampleIndexI1;

    @Column(name = "Sequenced_read_pairs")
    private Long sequencedReadPairs;

    @Column(name = "Sequencing_saturation")
    private Double sequencingSaturation;

    @Column(name = "TSS_enrichment_score")
    private Double tssEnrichmentScore;

    @Column(name = "Unmapped_read_pairs")
    private Double unmappedReadPairs;

    @Column(name = "Valid_barcodes")
    private Double validBarcodes;
}
