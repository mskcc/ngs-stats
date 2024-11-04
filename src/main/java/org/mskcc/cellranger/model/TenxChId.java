package org.mskcc.cellranger.model;

import java.io.Serializable;
import java.util.Objects;

public class TenxChId implements Serializable {
    private String sampleId;
    ;
    private String chSampleId;

    public TenxChId(String sampleId, String chSampleId) {
        this.sampleId = sampleId;
        this.chSampleId = chSampleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TenxChId tenxChId = (TenxChId) o;
        return Objects.equals(sampleId, tenxChId.sampleId) && Objects.equals(chSampleId, tenxChId.chSampleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sampleId, chSampleId);
    }
}
