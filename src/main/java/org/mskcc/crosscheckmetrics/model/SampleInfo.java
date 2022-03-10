package org.mskcc.crosscheckmetrics.model;
import lombok.Getter;
import lombok.AccessLevel;

import java.util.Objects;

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
    public SampleInfo(String patientId, String project, String igoId) {
        this.project = project;
        this.patientId = patientId;
        this.igoId = igoId;
    }
    public String getTumorNormal() {
        return this.tumorNormal.name();
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof SampleInfo)) {
            return false;
        }
        if(((SampleInfo) o).getProject().equals(this.getProject()) && ((SampleInfo) o).getIgoId().equals(this.getIgoId()) &&
                ((SampleInfo) o).getPatientId().equals(this.getPatientId()) && ((SampleInfo) o).getTumorNormal().equals(this.getTumorNormal())) {
            return true;
        }

        return false;
    }
}
