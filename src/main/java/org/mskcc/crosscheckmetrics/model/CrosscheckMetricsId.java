package org.mskcc.crosscheckmetrics.model;

import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@NoArgsConstructor
@Embeddable
public class CrosscheckMetricsId implements Serializable {
    public String getProject() {
        return project;
    }

    public String getIgoIdA() {
        return igoIdA;
    }

    public String getIgoIdB() {
        return igoIdB;
    }
    @Column(length = 64)
    private String project;
    @Column(length = 32)
    private String igoIdA;
    @Column(length = 32)
    private String igoIdB;

    public CrosscheckMetricsId(String project, String igoIdA, String igoIdB) {
        this.project = project;
        this.igoIdA = igoIdA;
        this.igoIdB = igoIdB;
    }
}
