package org.mskcc.cellranger.model;

import java.io.Serializable;
import java.util.Objects;

public class TenxAbcChId implements Serializable {
    private String sampleId;
    ;
    private String chSampleId;

    public TenxAbcChId(String sampleId, String chSampleId) {
        this.sampleId = sampleId;
        this.chSampleId = chSampleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TenxAbcChId that = (TenxAbcChId) o;
        return Objects.equals(sampleId, that.sampleId) && Objects.equals(chSampleId, that.chSampleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sampleId, chSampleId);
    }
}
