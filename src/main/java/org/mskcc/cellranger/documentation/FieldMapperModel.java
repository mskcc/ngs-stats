package org.mskcc.cellranger.documentation;

import java.util.List;

public abstract class FieldMapperModel {
    // List of fields that need to be parsed from the cell ranger output and saved into a data record
    protected List<FieldMapper> fieldMapperList;

    public List<FieldMapper> getFieldMapperList() {
        return fieldMapperList;
    }

    /**
     * Returns the type (Double, String) of the input field as it is labeled in the HTML
     *
     * @param field
     * @return
     */
    public Class getSqlType(String field) {
        for(FieldMapper fm : fieldMapperList){
            if(fm.getHtmlField().equals(field)){
                return fm.getType();
            }
        }
        return null;
    }

    /**
     * Returns the SQL column name of the input field as it is labeled in the HTML
     * 
     * @param field
     * @return
     */
    public String getSqlColumn(String field) {
        for(FieldMapper fm : fieldMapperList){
            if(fm.getHtmlField().equals(field)){
                return fm.getTableField();
            }
        }
        return null;
    }
}
