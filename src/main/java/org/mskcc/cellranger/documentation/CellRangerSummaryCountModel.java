package org.mskcc.cellranger.documentation;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * CREATES THE DAO & DOCUMENTATION
 *      MODIFY          When there are changes to the schema or CellRanger output
 *      DO NOT MODIFY   org.mskcc.cellranger.model files or /java/org/mskcc/cellranger/documentation/README.md
 */
public class CellRangerSummaryCountModel extends FieldMapperModel {
    public CellRangerSummaryCountModel() {
        fieldMapperList = new ArrayList<FieldMapper>(Arrays.asList(
                new FieldMapper("h1", "Estimated Number of Cells", "EstimatedNumberOfCells", Double.class),
                new FieldMapper("h2", "Mean Reads per Cell", "MeanReadsPerCell", Double.class),
                new FieldMapper("h2", "Median Genes per Cell", "MedianGenesPerCell", Double.class),
                new FieldMapper("td", "Number of Reads", "NumberOfReads", Double.class),
                new FieldMapper("td", "Valid Barcodes", "ValidBarcodes", Double.class),
                new FieldMapper("td", "Sequencing Saturation", "SequencingSaturation", Double.class),
                new FieldMapper("td", "Q30 Bases in Barcode", "Q30BasesInBarcode", Double.class),
                new FieldMapper("td", "Q30 Bases in RNA Read", "Q30BasesinRNARead", Double.class),
                new FieldMapper("td", "Q30 Bases in Sample Index", "Q30BasesInSampleIndex", Double.class),
                new FieldMapper("td", "Q30 Bases in UMI", "Q30BasesInUMI", Double.class),
                new FieldMapper("td", "Reads Mapped to Genome", "ReadsMappedToGenome", Double.class),
                new FieldMapper("td", "Reads Mapped Confidently to Genome", "ReadsMappedConfidentlyToGenome", Double.class),
                new FieldMapper("td", "Reads Mapped Confidently to Intergenic Regions", "ReadsMappedToIntergenicRegions", Double.class),
                new FieldMapper("td", "Reads Mapped Confidently to Intronic Regions", "ReadsMappedToIntronicRegions", Double.class),
                new FieldMapper("td", "Reads Mapped Confidently to Exonic Regions", "ReadsMappedToExonicRegions", Double.class),
                new FieldMapper("td", "Reads Mapped Confidently to Transcriptome", "ReadsMappedToTranscriptome", Double.class),
                new FieldMapper("td", "Reads Mapped Antisense to Gene", "ReadsMappedAntisenseToGene", Double.class),
                new FieldMapper("td", "Fraction Reads in Cells", "FractionReadsInCells", Double.class),
                new FieldMapper("td", "Total Genes Detected", "TotalGenesDetected", Double.class),
                new FieldMapper("td", "Median UMI Counts per Cell", "MedianUMICountsPerCell", Double.class),
                new FieldMapper("td", "Name", "Name", String.class),
                // Excluding Description because it is usually blank
                // new FieldMapper("td", "Description", "Description", String.class),
                new FieldMapper("td", "Transcriptome", "Transcriptome", String.class),
                new FieldMapper("td", "Chemistry", "Chemistry", String.class),
                new FieldMapper("td", "Cell Ranger Version", "CellRangerVersion", String.class),
                new FieldMapper("script", "N/A - Contained in script body", "CompressedGraphData", String.class)
        ));
    }
}
