package org.mskcc.cellranger.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

public class CellRangerDataRecord {
    private static Logger log = LoggerFactory.getLogger(CellRangerDataRecord.class);

    public void setField(String fieldName, String value, Class type){
        Field field;
        try{
            field = this.getClass().getField(fieldName);
        } catch(NoSuchFieldException e){
            log.error(String.format("Data record does not have field - %s. Error: %s", fieldName, e.getMessage()));
            return;
        }

        final String typeString = type.toString();

        if(typeString.equals(Float.class.toString())){
            setFloat(field,value);
        } else if(typeString.equals(Long.class.toString())){
            setLong(field,value);
        } else if(typeString.equals(String.class.toString())){
            setString(field,value);
        }
    }

    private void setFloat(Field field, String value){
        Float castValue;

        try {
            castValue = Float.parseFloat(value);
        } catch (NumberFormatException e){
            log.error(String.format("Error setting Float field: %s. Non-parsable value: %s. Error: %s", field.getName(), value, e.getMessage()));
            return;
        }

        try {
            field.set(this, castValue);
        } catch (IllegalAccessException e){
            log.error(String.format("Error setting Float field: %s. Error: %s", field.getName(), e.getMessage()));
            return;
        }
    }

    private void setLong(Field field, String value){
        Long castValue;
        try {
            castValue = Long.parseLong(value);
        } catch (Exception e){
            log.error(String.format("Error setting Long field: %s. Non-parsable value: %s. Error: %s", field.getName(), value, e.getMessage()));
            return;
        }

        try {
            field.set(this, castValue);
        } catch (IllegalAccessException e){
            log.error(String.format("Error setting Long field: %s. Error: %s", field.getName(), e.getMessage()));
            return;
        }
    }

    private void setString(Field field, String value){
        try {
            field.set(this, value);
        } catch (IllegalAccessException e){
            log.error(String.format("Error setting String field: %s. Error: %s", field.getName(), e.getMessage()));
            return;
        }
    }
}
