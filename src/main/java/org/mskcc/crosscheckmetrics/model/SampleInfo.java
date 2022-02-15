package org.mskcc.crosscheckmetrics.model;
import lombok.Getter;
import lombok.AccessLevel;
@Getter
public class SampleInfo {
    private String project;
    private String igoId;
    private String patientId;
    @Getter(AccessLevel.NONE) private TumorNormal tumorNormal;
    /* The existing getTumorNormal should override the default
     lombok automatic getter generation.*/

    public SampleInfo(String project, String igoId, String patientId, String tumorNormal) {
        this.project = project;
        this.igoId = igoId;
        this.patientId = patientId;
        this.tumorNormal = TumorNormal.getEnum(tumorNormal);
    }
    public SampleInfo(String project, String igoId, String patientId) {
        this.project = project;
        this.patientId = patientId;
        this.igoId = igoId;
    }
    public String getTumorNormal() {
        return this.tumorNormal.name();
    }
}
