package org.mskcc.crosscheckmetrics.model;

// Enum taken from Broad, CrosscheckMetrics::FingerprintResult
// https://github.com/broadinstitute/picard/blob/master/src/main/java/picard/fingerprint/CrosscheckMetric.java
public enum FingerprintResult {
    EXPECTED_MATCH(true, true),
    EXPECTED_MISMATCH(true, false),
    UNEXPECTED_MATCH(false, true),
    UNEXPECTED_MISMATCH(false, false),
    INCONCLUSIVE(null, null);

    private final Boolean isExpected;
    private final Boolean isMatch;

    FingerprintResult(Boolean isExpected, Boolean isMatch) {
        this.isExpected = isExpected;
        this.isMatch = isMatch;
    }

    public Boolean isExpected() {
        return isExpected;
    }

    public Boolean isMatch() {
        return isMatch;
    }
}
