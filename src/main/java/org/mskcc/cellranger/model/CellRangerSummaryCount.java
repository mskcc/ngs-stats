package org.mskcc.cellranger.model;

import java.lang.Double;
import java.lang.String;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.NoArgsConstructor;

/**
 * ─────────────────▄████▄
 * ─────▄▄▄▄▄▄▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▄▄▄▄▄▄▄
 * ───▄▀░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░▀▄
 * ──▐░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░▌
 * ──▐░░██████░░███████░░█████░░░██████░░▌
 * ──▐░░██░░░░░░░░██░░░░██░░░██░░██░░░██░▌
 * ──▐░░██████░░░░██░░░░██░░░██░░██████░░▌
 * ──▐░░░░░░██░░░░██░░░░██░░░██░░██░░░░░░▌
 * ──▐░░██████░░░░██░░░░░████░░░░██░░░░░░▌
 * ──▐░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░▌
 * ───▀▄░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░▄▀
 * ─────▀▀▀▀▀▀▀▀▀▀▀▀██████▀▀▀▀▀▀▀▀▀▀▀▀▀
 * ──────────────────█▀▀█
 * ──────────────────█▀▀█
 * ──────────────────█▀▀█
 * ──────────────────█▀▀█
 * ──────────────────█▀▀█
 * ──────────────────█▀▀█
 * ──────────────────█▀▀█
 * ──────────────────█▀▀█
 * ──────────────────█▀▀█
 * ──────────────────█▀▀█
 * ──────────────────█▀▀█
 * ──────────────────█▀▀█
 * ──────────────────█▀▀█
 * ──────────────────█▀▀█
 * ──────────────────█▀▀█
 * ──────────────────█▀▀█
 * ──────────────────█▀▀█
 * ─────────────────█▀▀▀▀█
 * ─────────────────█▀▀▀▀█
 * ─────────────────█▀▀▀▀█
 * ─────────────────█▀▀▀▀█
 * ─────────────────▀████▀ 
 *
 * ******************************************************************************
 * ***************************** DO NOT EDIT/DELETE *****************************
 * ****************************************************************************** */
@Entity
@NoArgsConstructor
public class CellRangerSummaryCount extends CellRangerDataRecord {
    @Id
    @Column(
            length = 128
    )
    public String id;

    @Column(
            length = 128
    )
    public String run;

    @Column(
            length = 128
    )
    public String project;

    public Double EstimatedNumberOfCells;

    public Double MeanReadsPerCell;

    public Double MedianGenesPerCell;

    public Double NumberOfReads;

    public Double ValidBarcodes;

    public Double SequencingSaturation;

    public Double Q30BasesInBarcode;

    public Double Q30BasesinRNARead;

    public Double Q30BasesInSampleIndex;

    public Double Q30BasesInUMI;

    public Double ReadsMappedToGenome;

    public Double ReadsMappedConfidentlyToGenome;

    public Double ReadsMappedToIntergenicRegions;

    public Double ReadsMappedToIntronicRegions;

    public Double ReadsMappedToExonicRegions;

    public Double ReadsMappedToTranscriptome;

    public Double ReadsMappedAntisenseToGene;

    public Double FractionReadsInCells;

    public Double TotalGenesDetected;

    public Double MedianUMICountsPerCell;

    @Column(
            length = 64
    )
    public String Name;

    @Column(
            length = 64
    )
    public String Transcriptome;

    @Column(
            length = 64
    )
    public String Chemistry;

    @Column(
            length = 64
    )
    public String CellRangerVersion;

    @Column(
            columnDefinition = "MEDIUMTEXT"
    )
    public String CompressedGraphData;
}
