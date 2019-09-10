package org.mskcc.cellranger.documentation;

import java.util.List;

public abstract class FieldMapperModel {
    // List of fields that need to be parsed from the cell ranger output and saved into a data record
    protected List<FieldMapper> fieldMapperList;

    public List<FieldMapper> getFieldMapperList(){
        return fieldMapperList;
    }
}
