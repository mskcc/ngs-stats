package org.mskcc.crosscheckmetrics.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@ToString
public class CrosscheckMetrics {
    @Id
    @Column(length=127)
    private String id;

    public Double lodScore;                  // lodScore scores from picard CrosscheckFingerprints
    public Double lodScoreTumorNormal;       // ... lodScore lodScoreTumorNormal lodScoreNormalTumor ...
    public Double lodScoreNormalTumor;

    public FingerprintResult result;

    @Column(length=63)
    public String project;

    @Column(length = 31)
    public String igoIdA;
    @Column(length = 31)
    public String igoIdB;

    @Column(length = 31)
    public String patientIdA;
    @Column(length = 31)
    public String patientIdB;

    public TumorNormal tumorNormalA;
    public TumorNormal tumorNormalB;

    @JsonInclude
    public boolean isExpected() {
        return result.isExpected();
    }

    public CrosscheckMetrics() {}
    public CrosscheckMetrics(Double lodScore, Double lodScoreTumorNormal, Double lodScoreNormalTumor, String project, String result, String[] projectAInfo, String[] projectBInfo){
        int SI_IGO_IDX = 1;
        int SI_PID_IDX = 2;
        int SI_TN_IDX = 3;

        this.lodScore = lodScore;
        this.lodScoreTumorNormal = lodScoreTumorNormal;
        this.lodScoreNormalTumor = lodScoreNormalTumor;
        this.result = FingerprintResult.valueOf(result);
        this.project = project;
        this.igoIdA = projectAInfo[SI_IGO_IDX];
        this.igoIdB = projectBInfo[SI_IGO_IDX];
        this.patientIdA = projectAInfo[SI_PID_IDX];
        this.patientIdB = projectBInfo[SI_PID_IDX];
        this.tumorNormalA = TumorNormal.getEnum(projectAInfo[SI_TN_IDX]);
        this.tumorNormalB = TumorNormal.getEnum(projectBInfo[SI_TN_IDX]);

        this.id = String.format("%s%s%s", project, this.igoIdA, this.igoIdB);
    }
}