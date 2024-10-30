package org.mskcc.cellranger.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;

@Entity
@Table(name = "10x_tcr")
@Data
public class TenxTcr {
    public static List<TenxLimsStats> toLimsStats(List<TenxTcr> inputList) {
        List<TenxLimsStats> list = new ArrayList<>();
        for (TenxTcr tenx : inputList) {
            TenxLimsStats lims = new TenxLimsStats();

            lims.setSampleId(tenx.getSampleId());
            lims.setSequencerRunFolder(tenx.getRunId());
            lims.setCellNumber(tenx.getEstimatedNumberOfCells().doubleValue());
            lims.setMeanReadsPerCell(tenx.getMeanReadPairsPerCell().doubleValue());
            lims.setMedianTraIghUmisPerCell(tenx.getMedianTraUmisPerCell());
            lims.setMedianTrbIgkUmisPerCell(tenx.getMedianTrbUmisPerCell());
            lims.setVdjReadsMapped(tenx.getReadsMappedToAnyVdjGene());
            lims.setTotalReads(tenx.getNumberOfReadPairs());

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

    @Column(name = "Estimated_Number_of_Cells")
    private Long estimatedNumberOfCells;

    @Column(name = "Mean_Read_Pairs_per_Cell")
    private Long meanReadPairsPerCell;

    @Column(name = "Number_of_Cells_with_Productive_VJ_Spanning_Pair")
    private Long numberOfCellsWithProductiveVjSpanningPair;

    @Column(name = "Number_of_Read_Pairs")
    private Long numberOfReadPairs;

    @Column(name = "Valid_Barcodes")
    private Double validBarcodes;

    @Column(name = "Q30_Bases_in_Barcode")
    private Double q30BasesInBarcode;

    @Column(name = "Q30_Bases_in_RNA_Read_1")
    private Double q30BasesInRnaRead1;

    @Column(name = "Q30_Bases_in_UMI")
    private Double q30BasesInUmi;

    @Column(name = "Reads_Mapped_to_Any_VDJ_Gene")
    private Double readsMappedToAnyVdjGene;

    @Column(name = "Reads_Mapped_to_TRA")
    private Double readsMappedToTra;

    @Column(name = "Reads_Mapped_to_TRB")
    private Double readsMappedToTrb;

    @Column(name = "Mean_Used_Read_Pairs_per_Cell")
    private Long meanUsedReadPairsPerCell;

    @Column(name = "Fraction_Reads_in_Cells")
    private Double fractionReadsInCells;

    @Column(name = "Median_TRA_UMIs_per_Cell")
    private Double medianTraUmisPerCell;

    @Column(name = "Median_TRB_UMIs_per_Cell")
    private Double medianTrbUmisPerCell;

    @Column(name = "Cells_with_Productive_VJ_Spanning_Pair")
    private Double cellsWithProductiveVjSpanningPair;

    @Column(name = "Cells_with_Productive_VJ_Spanning_TRA_TRB_Pair")
    private Double cellsWithProductiveVjSpanningTraTrbPair;

    @Column(name = "Paired_Clonotype_Diversity")
    private Double pairedClonotypeDiversity;

    @Column(name = "Cells_with_TRA_Contig")
    private Double cellsWithTraContig;

    @Column(name = "Cells_with_TRB_Contig")
    private Double cellsWithTrbContig;

    @Column(name = "Cells_with_CDR3_Annotated_TRA_Contig")
    private Double cellsWithCdr3AnnotatedTraContig;

    @Column(name = "Cells_with_CDR3_Annotated_TRB_Contig")
    private Double cellsWithCdr3AnnotatedTrbContig;

    @Column(name = "Cells_with_VJ_Spanning_TRA_Contig")
    private Double cellsWithVjSpanningTraContig;

    @Column(name = "Cells_with_VJ_Spanning_TRB_Contig")
    private Double cellsWithVjSpanningTrbContig;

    @Column(name = "Cells_with_Productive_TRA_Contig")
    private Double cellsWithProductiveTraContig;

    @Column(name = "Cells_with_Productive_TRB_Contig")
    private Double cellsWithProductiveTrbContig;
}