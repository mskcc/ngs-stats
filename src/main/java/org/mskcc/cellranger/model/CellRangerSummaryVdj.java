package org.mskcc.cellranger.model;

import java.lang.Float;
import java.lang.Long;
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
 * ******************************** DO NOT EDIT *********************************
 * ****************************************************************************** */
@Entity
@NoArgsConstructor
public class CellRangerSummaryVdj extends CellRangerDataRecord {
    @Id
    @Column(
            length = 128
    )
    public String id;

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
}
