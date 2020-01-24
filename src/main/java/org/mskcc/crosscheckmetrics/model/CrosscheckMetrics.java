package org.mskcc.crosscheckmetrics.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@ToString
public class CrosscheckMetrics {
    @Id
    @GeneratedValue
    private Long id;

    public Double lod;                // LOD score from picard CrosscheckFingerprints
    public Double lodAlt1;
    public Double lodAlt2;

    public FingerprintResult result;

    @Column(length=64)
    public String project;

    @Column(length = 32)
    public String igoIdA;
    @Column(length = 32)
    public String igoIdB;

    @Column(length = 32)
    public String patientIdA;
    @Column(length = 32)
    public String patientIdB;

    public TumorNormal tumorNormalA;
    public TumorNormal tumorNormalB;

    @JsonInclude
    public boolean isExpected() {
        return result.isExpected();
    }

    public CrosscheckMetrics(Double lod, Double lodAlt1, Double lodAlt2, String project, String result, String[] projectAInfo, String[] projectBInfo){
        int SI_IGO_IDX = 1;
        int SI_PID_IDX = 2;
        int SI_TN_IDX = 3;

        this.lod = lod;
        this.lodAlt1 = lodAlt1;
        this.lodAlt2 = lodAlt2;
        this.result = FingerprintResult.valueOf(result);
        this.project = project;
        this.igoIdA = projectAInfo[SI_IGO_IDX];
        this.igoIdB = projectBInfo[SI_IGO_IDX];
        this.patientIdA = projectAInfo[SI_PID_IDX];
        this.patientIdB = projectBInfo[SI_PID_IDX];
        this.tumorNormalA = TumorNormal.getEnum(projectAInfo[SI_TN_IDX]);
        this.tumorNormalB = TumorNormal.getEnum(projectBInfo[SI_TN_IDX]);
    }
}