package org.mskcc.cellranger.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

/**
 * Data Record that will be written to the database. Responsible for setting fields on itself of various types
 */
public class CellRangerDataRecord {
    private static Logger log = LoggerFactory.getLogger(CellRangerDataRecord.class);

    /**
     * Sets the field value by the Class type passed in
     *
     * @param fieldName
     * @param value
     * @param type
     */
    public void setField(String fieldName, String value, Class type) {
        Field field;
        try {
            field = this.getClass().getField(fieldName);
        } catch (NoSuchFieldException e) {
            log.error(String.format("Data record does not have field - %s. Error: %s", fieldName, e.getMessage()));
            return;
        }

        final String typeString = type.toString();

        if (typeString.equals(Double.class.toString())) {
            setDouble(field, value);
        } else if (typeString.equals(String.class.toString())) {
            setString(field, value);
        }
    }

    private void setDouble(Field field, String value) {
        Double castValue;

        try {
            castValue = Double.parseDouble(value);
        } catch (NumberFormatException e) {
            log.error(String.format("Error setting Double field: %s. Non-parsable value: %s. Error: %s", field.getName(), value, e.getMessage()));
            return;
        }

        try {
            field.set(this, castValue);
        } catch (IllegalAccessException e) {
            log.error(String.format("Error setting Double field: %s. Error: %s", field.getName(), e.getMessage()));
            return;
        }
    }

    private void setString(Field field, String value) {
        try {
            field.set(this, value);
        } catch (IllegalAccessException e) {
            log.error(String.format("Error setting String field: %s. Error: %s", field.getName(), e.getMessage()));
            return;
        }
    }
}
