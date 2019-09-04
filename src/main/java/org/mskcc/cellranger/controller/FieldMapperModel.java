package org.mskcc.cellranger.controller;

import java.util.List;

public abstract class FieldMapperModel {
    protected List<FieldMapper> fieldMapperList;
    public List<FieldMapper> getFieldMapperList(){
        return fieldMapperList;
    }
}
