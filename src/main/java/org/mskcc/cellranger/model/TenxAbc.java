package org.mskcc.cellranger.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Entity
@Table(name = "10x_abc")
@Getter @Setter @ToString
public class TenxAbc {
    public static List<TenxLimsStats> toLimsStats(List<TenxAbc> tenxAbcList) {
        List<TenxLimsStats> list = new ArrayList<>();
        for (TenxAbc tenx : tenxAbcList) {
            TenxLimsStats lims = new TenxLimsStats();
            lims.setSampleId(tenx.getSampleId());
            lims.setSequencerRunFolder(tenx.getRunId());
            lims.setCellNumber(tenx.getEstimatedNumberOfCells().doubleValue());
            lims.setMeanReadsPerCell(tenx.getMeanReadsPerCell().doubleValue());
            lims.setSeqSaturation(tenx.getSequencingSaturation());
            lims.setTotalReads(tenx.getNumberOfReads().intValue());
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

    @Column(name = "Mean_antibody_reads_usable_per_cell")
    private Long meanAntibodyReadsUsablePerCell;

    @Column(name = "Median_UMI_counts_per_cell")
    private Long medianUmiCountsPerCell;

    @Column(name = "GEX_Cells")
    private Long gexCells;

    @Column(name = "GEX_Confidently_mapped_reads_in_cells")
    private Double gexConfidentlyMappedReadsInCells;

    @Column(name = "GEX_Median_UMI_counts_per_cell")
    private Long gexMedianUmiCountsPerCell;

    @Column(name = "GEX_Median_genes_per_cell")
    private Long gexMedianGenesPerCell;

    @Column(name = "GEX_Total_genes_detected")
    private Long gexTotalGenesDetected;

    @Column(name = "Estimated_number_of_cells")
    private Long estimatedNumberOfCells;

    @Column(name = "Fraction_antibody_reads")
    private Double fractionAntibodyReads;

    @Column(name = "Fraction_antibody_reads_in_aggregate_barcodes")
    private Double fractionAntibodyReadsInAggregateBarcodes;

    @Column(name = "Fraction_antibody_reads_usable")
    private Double fractionAntibodyReadsUsable;

    @Column(name = "Mean_reads_per_cell")
    private Long meanReadsPerCell;

    @Column(name = "Number_of_reads")
    private Long numberOfReads;

    @Column(name = "Sequencing_saturation")
    private Double sequencingSaturation;

    @Column(name = "Valid_UMIs")
    private Double validUmis;

    @Column(name = "Valid_barcodes")
    private Double validBarcodes;

}