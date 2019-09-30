package org.mskcc.cellranger.documentation;

import java.util.ArrayList;
import java.util.Arrays;

public class CellRangerSummaryVDJModel extends FieldMapperModel {
    public CellRangerSummaryVDJModel() {
        fieldMapperList = new ArrayList<FieldMapper>(Arrays.asList(
                new FieldMapper("h1", "Estimated Number of Cells", "EstimatedNumberOfCells", Double.class),
                new FieldMapper("h2", "Mean Read Pairs per Cell", "MeanReadsPerCell",Double.class),
                new FieldMapper("h2", "Number of Cells With Productive V-J Spanning Pair", "NumCellsWithVDJSpanningPair", Double.class),
                new FieldMapper("td", "Reads Mapped to Any V(D)J Gene", "ReadsMappedToAnyVDJGene", Double.class),
                new FieldMapper("td", "Reads Mapped to TRA", "ReadsMappedToTRA", Double.class),
                new FieldMapper("td", "Reads Mapped to TRB", "ReadsMappedToTRB", Double.class),
                new FieldMapper("td", "Median TRA UMIs per Cell", "MedianTRAUMIsPerCell", Double.class),
                new FieldMapper("td", "Median TRB UMIs per Cell", "MedianTRBUMIsPerCell", Double.class),
                new FieldMapper("td", "Number of Read Pairs", "NumberOfReadPairs", Double.class),
                new FieldMapper("td", "Valid Barcodes", "ValidBarcodes", Double.class),
                new FieldMapper("td", "Q30 Bases in Barcode", "Q30BasesInBarcode", Double.class),
                new FieldMapper("td", "Q30 Bases in RNA Read 1", "Q30BasesInRNARead1", Double.class),
                new FieldMapper("td", "Q30 Bases in RNA Read 2", "Q30BasesInRNARead2", Double.class),
                new FieldMapper("td", "Q30 Bases in Sample Index", "Q30BasesInSampleIndex", Double.class),
                new FieldMapper("td", "Q30 Bases in UMI", "Q30BasesInUMI", Double.class),
                new FieldMapper("td", "Name", "Name", String.class),
                // Excluding Description because it is usually blank
                // new FieldMapper("td", "Description", "Description", String.class),
                new FieldMapper("td", "V(D)J Reference", "VDJReference", String.class),
                new FieldMapper("td", "Chemistry", "Chemistry", String.class),
                new FieldMapper("td", "Cell Ranger Version", "CellRangerVersion", String.class),
                new FieldMapper("script", "N/A - Contained in script body", "CompressedGraphData", String.class)
        ));
    }
}
