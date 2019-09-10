package org.mskcc.cellranger.model;

import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

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
 * ******************************** DO NOT EDIT *********************************
 * ****************************************************************************** */
@Entity
@NoArgsConstructor
public class CellRangerSummaryCount extends CellRangerDataRecord {
    public Long EstimatedNumberOfCells;

    public Long MeanReadsPerCell;

    public Long MedianGenesPerCell;

    public Long NumberOfReads;

    public Float ValidBarcodes;

    public Float SequencingSaturation;

    public Float Q30BasesInBarcode;

    public Float Q30BasesinRNARead;

    public Float Q30BasesInSampleIndex;

    public Float Q30BasesInUMI;

    public Float ReadsMappedToGenome;

    public Float ReadsMappedConfidentlyToGenome;

    public Float ReadsMappedToIntergenicRegions;

    public Float ReadsMappedToIntronicRegions;

    public Float ReadsMappedToExonicRegions;

    public Float ReadsMappedToTranscriptome;

    public Float ReadsMappedToGene;

    public Float FractionReadsInCells;

    public Long TotalGenesDetected;

    public Long MedianUMICountsPerCell;

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

    @Id
    @Column(
            length = 150
    )
    public String id;
}
