package org.mskcc.crosscheckmetrics.model;
import lombok.Getter;
@Getter
public class SampleInfo {
    private String project;
    private String igoId;
    private String patientId;
    private TumorNormal tumorNormal;

    public SampleInfo(String project, String igoId, String patientId, String tumorNormal) {
        this.project = project;
        this.igoId = igoId;
        this.patientId = patientId;
        this.tumorNormal = TumorNormal.getEnum(tumorNormal);
    }

    public String getProject() {
        return this.project;
    }

    public String getIgoId() {
        return this.igoId;
    }

    public String getPatientId() {
        return this.patientId;
    }

    public String getTumorNormal() {
        return this.tumorNormal.name();
    }
}
