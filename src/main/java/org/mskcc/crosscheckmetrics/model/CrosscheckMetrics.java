package org.mskcc.crosscheckmetrics.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
@NoArgsConstructor
@ToString
public class CrosscheckMetrics {
    public Double lodScore;                  // lodScore scores from picard CrosscheckFingerprints
    public Double lodScoreTumorNormal;       // ... lodScore lodScoreTumorNormal lodScoreNormalTumor ...
    public Double lodScoreNormalTumor;
    public String result;
    @Column(length = 31)
    public String patientIdA;
    @Column(length = 31)
    public String patientIdB;
    public String tumorNormalA;
    public String tumorNormalB;
    @EmbeddedId
    public CrosscheckMetricsId crosscheckMetricsId;

    public CrosscheckMetricsId getId(){
        return this.crosscheckMetricsId;
    }

    public CrosscheckMetrics(Double lodScore, Double lodScoreTumorNormal, Double lodScoreNormalTumor, String project, String result, SampleInfo sampleAInfo, SampleInfo sampleBInfo) {
        this.lodScore = lodScore;
        this.lodScoreTumorNormal = lodScoreTumorNormal;
        this.lodScoreNormalTumor = lodScoreNormalTumor;
        this.result = result;
        this.patientIdA = sampleAInfo.getPatientId();
        this.patientIdB = sampleBInfo.getPatientId();
        this.tumorNormalA = sampleAInfo.getTumorNormal();
        this.tumorNormalB = sampleBInfo.getTumorNormal();
        this.crosscheckMetricsId = new CrosscheckMetricsId(project, sampleAInfo.getIgoId(), sampleBInfo.getIgoId());
    }

    @JsonInclude
    public Boolean isExpected() {
        return FingerprintResult.valueOf(result).isExpected();
    }
}