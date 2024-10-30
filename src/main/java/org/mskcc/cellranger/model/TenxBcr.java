package org.mskcc.cellranger.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;

@Entity
@Table(name = "10x_bcr")
@Data
public class TenxBcr {
    public static List<TenxLimsStats> toLimsStats(List<TenxBcr> inputList) {
        List<TenxLimsStats> list = new ArrayList<>();
        for (TenxBcr tenx : inputList) {
            TenxLimsStats lims = new TenxLimsStats();

            lims.setSampleId(tenx.getSampleId());
            lims.setSequencerRunFolder(tenx.getRunId());
            lims.setCellNumber(tenx.getEstimatedNumberOfCells().doubleValue());
            lims.setMeanReadsPerCell(tenx.getMeanReadPairsPerCell().doubleValue());
            lims.setMedianIGLUmisPerCell(tenx.getMedianIglUmisPerCell());
            lims.setMedianTraIghUmisPerCell(tenx.getMedianIghUmisPerCell());
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

    @Column(name = "Number_of_Cells_With_Productive_VJ_Spanning_Pair")
    private Long numberOfCellsWithProductiveVJSpanningPair;

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

    @Column(name = "Reads_Mapped_to_IGH")
    private Double readsMappedToIgh;

    @Column(name = "Reads_Mapped_to_IGK")
    private Double readsMappedToIgk;

    @Column(name = "Reads_Mapped_to_IGL")
    private Double readsMappedToIgl;

    @Column(name = "Mean_Used_Read_Pairs_per_Cell")
    private Long meanUsedReadPairsPerCell;

    @Column(name = "Fraction_Reads_in_Cells")
    private Double fractionReadsInCells;

    @Column(name = "Median_IGH_UMIs_per_Cell")
    private Double medianIghUmisPerCell;

    @Column(name = "Median_IGK_UMIs_per_Cell")
    private Double medianIgkUmisPerCell;

    @Column(name = "Median_IGL_UMIs_per_Cell")
    private Double medianIglUmisPerCell;

    @Column(name = "Cells_With_Productive_VJ_Spanning_Pair")
    private Double cellsWithProductiveVJSpanningPair;

    @Column(name = "Cells_With_Productive_VJ_Spanning_IGK_IGH_Pair")
    private Double cellsWithProductiveVJSpanningIgkIghPair;

    @Column(name = "Cells_With_Productive_VJ_Spanning_IGL_IGH_Pair")
    private Double cellsWithProductiveVJSpanningIglIghPair;

    @Column(name = "Paired_Clonotype_Diversity")
    private Double pairedClonotypeDiversity;

    @Column(name = "Cells_With_IGH_Contig")
    private Double cellsWithIghContig;

    @Column(name = "Cells_With_IGK_Contig")
    private Double cellsWithIgkContig;

    @Column(name = "Cells_With_IGL_Contig")
    private Double cellsWithIglContig;

    @Column(name = "Cells_With_CDR3_Annotated_IGH_Contig")
    private Double cellsWithCdr3AnnotatedIghContig;

    @Column(name = "Cells_With_CDR3_Annotated_IGK_Contig")
    private Double cellsWithCdr3AnnotatedIgkContig;

    @Column(name = "Cells_With_CDR3_Annotated_IGL_Contig")
    private Double cellsWithCdr3AnnotatedIglContig;

    @Column(name = "Cells_With_VJ_Spanning_IGH_Contig")
    private Double cellsWithVjSpanningIghContig;

    @Column(name = "Cells_With_VJ_Spanning_IGK_Contig")
    private Double cellsWithVjSpanningIgkContig;

    @Column(name = "Cells_With_VJ_Spanning_IGL_Contig")
    private Double cellsWithVjSpanningIglContig;

    @Column(name = "Cells_With_Productive_IGH_Contig")
    private Double cellsWithProductiveIghContig;

    @Column(name = "Cells_With_Productive_IGK_Contig")
    private Double cellsWithProductiveIgkContig;

    @Column(name = "Cells_With_Productive_IGL_Contig")
    private Double cellsWithProductiveIglContig;
}