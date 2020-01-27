package org.mskcc.crosscheckmetrics.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@ToString
public class CrosscheckMetrics {
    public Double lodScore;                  // lodScore scores from picard CrosscheckFingerprints
    public Double lodScoreTumorNormal;       // ... lodScore lodScoreTumorNormal lodScoreNormalTumor ...
    public Double lodScoreNormalTumor;
    public String result;
    @Column(length = 63)
    public String project;
    @Column(length = 31)
    public String igoIdA;
    @Column(length = 31)
    public String igoIdB;
    @Column(length = 31)
    public String patientIdA;
    @Column(length = 31)
    public String patientIdB;
    public String tumorNormalA;
    public String tumorNormalB;
    @Id
    @Column(length = 127)
    private String id;

    public CrosscheckMetrics(Double lodScore, Double lodScoreTumorNormal, Double lodScoreNormalTumor, String project, String result, SampleInfo sampleAInfo, SampleInfo sampleBInfo) {
        this.lodScore = lodScore;
        this.lodScoreTumorNormal = lodScoreTumorNormal;
        this.lodScoreNormalTumor = lodScoreNormalTumor;
        this.result = result;
        this.project = project;
        this.igoIdA = sampleAInfo.getIgoId();
        this.igoIdB = sampleBInfo.getIgoId();
        this.patientIdA = sampleAInfo.getPatientId();
        this.patientIdB = sampleBInfo.getPatientId();
        this.tumorNormalA = sampleAInfo.getTumorNormal();
        this.tumorNormalB = sampleBInfo.getTumorNormal();

        this.id = String.format("%s%s%s", project, this.igoIdA, this.igoIdB);
    }

    @JsonInclude
    public boolean isExpected() {
        return FingerprintResult.valueOf(result).isExpected();
    }
}