package org.mskcc.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ParserUtil {
    private static Logger log = LoggerFactory.getLogger(ParserUtil.class);

    /**
     * Safe conversion of a string to a float with logging
     */
    public static Double parseDouble(String st) {
        try {
            Double val = Double.parseDouble(st);
            return val;
        } catch (NumberFormatException e) {
            // TODO - Add splunk alert for this error in the logs
            log.error(String.format("Failed to convert %s to float: %s", st, e.getMessage()));
        }
        return null;
    }

    /**
     * Parses out the cleaned (no comma, quotes, etc.) in the line
     *      E.g.    "152,952,980",97.1%,81.5%,...   =>  152952980,0.971,0.815%,...
     * @param line
     * @return
     */
    public static List<String> parseCellRangerCsvLine(String line){
        String[] values = line.split("[\"%],|,[\"%]");
        List<String> cleanedValues = new ArrayList<>();
        for(String value : values){
            value = value.replace(",", "");
            value = value.replace("\"", "");
            value = value.replace("%", "");
            if(value.contains(".")){
                BigDecimal d = new BigDecimal(value).divide(BigDecimal.valueOf(100));
                if(! (d.compareTo(new BigDecimal(1)) > 1) ){
                    // If the value is a decimal (less than or equal to 1), make the double the value
                    value = d.toString();
                }
            }
            cleanedValues.add(value);
        }

        return cleanedValues;
    }
}
