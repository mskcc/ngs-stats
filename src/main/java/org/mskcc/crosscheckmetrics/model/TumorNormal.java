package org.mskcc.crosscheckmetrics.model;

public enum TumorNormal {
    Tumor, Normal, Unknown;

    public static TumorNormal getEnum(String value) {
        switch (value.toLowerCase()) {
            case "tumor":
                return TumorNormal.Tumor;
            case "normal":
                return TumorNormal.Normal;
            default:
                break;
        }
        return TumorNormal.Unknown;
    }
}
