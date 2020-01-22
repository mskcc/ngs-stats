package org.mskcc.crosscheckmetrics.model;

import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@ToString
public class CrosscheckMetrics {
    public CrosscheckMetrics(Float lod, String result, String[] projectAInfo, String[] projectBInfo){
        int SI_PRJ_IDX = 0;
        int SI_IGO_IDX = 1;
        int SI_PID_IDX = 2;

        this.lod = lod;
        this.result = result;
        this.project_A = projectAInfo[SI_PRJ_IDX];
        this.project_B = projectBInfo[SI_PRJ_IDX];
        this.igoId_A = projectAInfo[SI_IGO_IDX];
        this.igoId_B = projectBInfo[SI_IGO_IDX];
        this.patientId_A = projectAInfo[SI_PID_IDX];
        this.patientId_B = projectBInfo[SI_PID_IDX];
    }

    @Id
    @GeneratedValue
    @Column(updatable = false, nullable = false)
    private Long id;

    @Column()
    public Float lod;               // LOD score from picard CrosscheckFingerprints

    @Column()
    public String result;           // Result from picard CrosscheckFingerprints

    @Column(length=64)
    public String project_A;
    @Column(length=64)
    public String project_B;        // If comparing across projects, project_B is the source of *_B fields

    @Column(length = 32)
    public String igoId_A;
    @Column(length = 32)
    public String igoId_B;

    @Column(length = 32)
    public String patientId_A;
    @Column(length = 32)
    public String patientId_B;
}