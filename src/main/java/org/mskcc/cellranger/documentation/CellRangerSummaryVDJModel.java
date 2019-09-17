package org.mskcc.cellranger.documentation;

import java.util.ArrayList;
import java.util.Arrays;

public class CellRangerSummaryVDJModel extends FieldMapperModel {
    public CellRangerSummaryVDJModel() {
        fieldMapperList = new ArrayList<FieldMapper>(Arrays.asList(
                new FieldMapper("h1", "Estimated Number of Cells", "EstimatedNumberOfCells", Long.class),
                new FieldMapper("h2", "Mean Read Pairs per Cell", "MeanReadsPerCell",Long.class),
                new FieldMapper("h2", "Number of Cells With Productive V-J Spanning Pair", "NumCellsWithVDJSpanningPair", Long.class),
                new FieldMapper("td", "Reads Mapped to Any V(D)J Gene", "ReadsMappedToAnyVDJGene", Float.class),
                new FieldMapper("td", "Reads Mapped to TRA", "ReadsMappedToTRA", Float.class),
                new FieldMapper("td", "Reads Mapped to TRB", "ReadsMappedToTRB", Float.class),
                new FieldMapper("td", "Median TRA UMIs per Cell", "MedianTRAUMIsPerCell", Float.class),
                new FieldMapper("td", "Median TRB UMIs per Cell", "MedianTRBUMIsPerCell", Float.class),
                new FieldMapper("td", "Number of Read Pairs", "NumberOfReadPairs", Long.class),
                new FieldMapper("td", "Valid Barcodes", "ValidBarcodes", Float.class),
                new FieldMapper("td", "Q30 Bases in Barcode", "Q30BasesInBarcode", Float.class),
                new FieldMapper("td", "Q30 Bases in RNA Read 1", "Q30BasesInRNARead1", Float.class),
                new FieldMapper("td", "Q30 Bases in RNA Read 2", "Q30BasesInRNARead2", Float.class),
                new FieldMapper("td", "Q30 Bases in Sample Index", "Q30BasesInSampleIndex", Float.class),
                new FieldMapper("td", "Q30 Bases in UMI", "Q30BasesInUMI", Float.class),
                new FieldMapper("td", "Name", "Name", String.class),
                // Excluding Description because it is usually blank
                // new FieldMapper("td", "Description", "Description", String.class),
                new FieldMapper("td", "V(D)J Reference", "VDJReference", String.class),
                new FieldMapper("td", "Chemistry", "Chemistry", String.class),
                new FieldMapper("td", "Cell Ranger Version", "CellRangerVersion", String.class)
        ));
    }
}
