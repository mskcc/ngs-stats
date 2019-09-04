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
public class CellRangerSummaryVdj extends FieldSetter {
    public Long EstimatedNumberOfCells;

    public Long MeanReadsPerCell;

    public Long NumCellsWithVDJSpanningPair;

    public Float ReadsMappedToAnyVDJGene;

    public Float ReadsMappedToTRA;

    public Float ReadsMappedToTRB;

    public Float MedianTRAUMIsPerCell;

    public Float MedianTRBUMIsPerCell;

    public Long NumberOfReadPairs;

    public Float ValidBarcodes;

    public Float Q30BasesInBarcode;

    public Float Q30BasesInRNARead1;

    public Float Q30BasesInRNARead2;

    public Float Q30BasesInSampleIndex;

    public Float Q30BasesInUMI;

    @Column(
            length = 64
    )
    public String Name;

    @Column(
            length = 64
    )
    public String VDJReference;

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
