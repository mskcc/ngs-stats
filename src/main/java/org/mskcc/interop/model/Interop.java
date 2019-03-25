package org.mskcc.interop.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "interop")
@IdClass(Interop.InteropId.class)
public class Interop implements Serializable {
    @Id private String runFolder;
    private String runId;
    @Id private int readNumber;
    @Id private int lane;
    private float density;
    private float densityStddev;
    private float percentPf;
    private float percentPfStddev;
    @Column(name = "reads_m")
    private float readsM;
    @Column(name = "reads_pf")
    private float readsPf;
    @Column(name = "percent_gt_q30")
    private float percentGtQ30;
    private float percentAligned;
    @Column(name = "percent_aligned_stddev")
    private float percentAlignedStddev;
    private float errorRate;
    private float errorRateStddev;

    public static class InteropId implements Serializable {
        private String runFolder;
        private int readNumber;
        private int lane;

        public InteropId() {}

        public InteropId(String runFolder, int readNumber, int lane) {
            this.runFolder = runFolder;
            this.readNumber = readNumber;
            this.lane = lane;
        }

        public String getRunFolder() {
            return runFolder;
        }

        public void setRunFolder(String runFolder) {
            this.runFolder = runFolder;
        }

        public int getReadNumber() {
            return readNumber;
        }

        public void setReadNumber(int readNumber) {
            this.readNumber = readNumber;
        }

        public int getLane() {
            return lane;
        }

        public void setLane(int lane) {
            this.lane = lane;
        }
    }

    public String getRunFolder() {
        return runFolder;
    }

    public void setRunFolder(String runFolder) {
        this.runFolder = runFolder;
    }

    public String getRunId() {
        return runId;
    }

    public void setRunId(String runId) {
        this.runId = runId;
    }

    public int getReadNumber() {
        return readNumber;
    }

    public void setReadNumber(int readNumber) {
        this.readNumber = readNumber;
    }

    public int getLane() {
        return lane;
    }

    public void setLane(int lane) {
        this.lane = lane;
    }

    public float getDensity() {
        return density;
    }

    public void setDensity(float density) {
        this.density = density;
    }

    public float getDensityStddev() {
        return densityStddev;
    }

    public void setDensityStddev(float densityStddev) {
        this.densityStddev = densityStddev;
    }

    public float getPercentPf() {
        return percentPf;
    }

    public void setPercentPf(float percentPf) {
        this.percentPf = percentPf;
    }

    public float getPercentPfStddev() {
        return percentPfStddev;
    }

    public void setPercentPfStddev(float percentPfStddev) {
        this.percentPfStddev = percentPfStddev;
    }

    public float getReadsM() {
        return readsM;
    }

    public void setReadsM(float readsM) {
        this.readsM = readsM;
    }

    public float getReadsPf() {
        return readsPf;
    }

    public void setReadsPf(float readsPf) {
        this.readsPf = readsPf;
    }

    public float getPercentGtQ30() {
        return percentGtQ30;
    }

    public void setPercentGtQ30(float percentGtQ30) {
        this.percentGtQ30 = percentGtQ30;
    }

    public float getPercentAligned() {
        return percentAligned;
    }

    public void setPercentAligned(float percentAligned) {
        this.percentAligned = percentAligned;
    }

    public float getPercentAlignedStddev() {
        return percentAlignedStddev;
    }

    public void setPercentAlignedStddev(float percentAlignedStddev) {
        this.percentAlignedStddev = percentAlignedStddev;
    }

    public float getErrorRate() {
        return errorRate;
    }

    public void setErrorRate(float errorRate) {
        this.errorRate = errorRate;
    }

    public float getErrorRateStddev() {
        return errorRateStddev;
    }

    public void setErrorRateStddev(float errorRateStddev) {
        this.errorRateStddev = errorRateStddev;
    }
}
