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
public class CellRangerSummaryVdj extends CellRangerDataRecord {
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

    public Double NumCellsWithVDJSpanningPair;

    public Double ReadsMappedToAnyVDJGene;

    public Double ReadsMappedToTRA;

    public Double ReadsMappedToTRB;

    public Double MedianTRAUMIsPerCell;

    public Double MedianTRBUMIsPerCell;

    public Double NumberOfReadPairs;

    public Double ValidBarcodes;

    public Double Q30BasesInBarcode;

    public Double Q30BasesInRNARead1;

    public Double Q30BasesInRNARead2;

    public Double Q30BasesInSampleIndex;

    public Double Q30BasesInUMI;

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

    @Column(
            columnDefinition = "MEDIUMTEXT"
    )
    public String CompressedGraphData;
}
