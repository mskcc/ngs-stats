package org.mskcc.cellranger.model;

import java.lang.reflect.Field;

// TODO - Rename
public class FieldSetter {
    public void setField(String fieldName, String value, Class type){
        Field field;
        try{
            field = this.getClass().getField(fieldName);
        } catch(NoSuchFieldException e){
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
        } catch (Exception e){
            // TODO - logging
            return;
        }

        try {
            field.setFloat(this, castValue);
        } catch (IllegalAccessException e){
            // TODO - logging
            return;
        }
    }

    private void setLong(Field field, String value){
        Long castValue;
        try {
            castValue = Long.parseLong(value);
        } catch (Exception e){
            // TODO - logging
            return;
        }

        try {
            field.setLong(this, castValue);
        } catch (IllegalAccessException e){
            // TODO - logging
            return;
        }
    }

    private void setString(Field field, String value){
        try {
            field.set(this, value);
        } catch (IllegalAccessException e){
            // TODO - logging
            return;
        }
    }
}
