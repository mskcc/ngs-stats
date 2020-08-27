package org.mskcc.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
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
     * Removes empty elements from an input Object array
     * @param inputArray, Object array
     * @param <E>
     * @return
     */
    private static <E> List<E> removeEmptiesFromArray(E[] inputArray) {
        final Object[] BLACKLIST = { "", null };        // Elements that will be removed from input

        List<E> list = new ArrayList<E>(Arrays.asList(inputArray));
        list.removeAll(Arrays.asList(BLACKLIST));

        return list;
    }

    /**
     * Parses out the cleaned (no comma, quotes, etc.) in the line
     *      E.g.    "152,952,980",97.1%,81.5%,...   =>  152952980,0.971,0.815%,...
     * @param line
     * @return
     */
    public static List<String> parseCellRangerCsvLine(String line){
        /**
         * Split the input line on the quote - this will "cleanly" (remove t separate all numbers with a comma in them
         *      e.g. line:          69,"32,143",1,"2,217,916",90.7%,0.0%,39,1
         *           quoteSplit:    ["69,", "32,143", ",1," , "2,217,916", ",90.7%,0.0%,39,1"
         */
        List<String> rawValues = new ArrayList<>();
        final String[] quoteSplitRaw = line.split("\"");
        List<String> quoteSplit = removeEmptiesFromArray(quoteSplitRaw);
        for(String quoteStr : quoteSplit){
            if( quoteStr.equals(",")){
                /**
                 *  A comma will be isolated if surrounded by two quoted. We want to skip this value
                 *      e.g. line: "\"2,083\",\"73,429\"" -> quoteSplit: ["2,083", ",", "73,429"]
                 */
                continue;
            }
            else if( quoteStr.charAt(0) == ',' ||
                quoteStr.charAt(quoteStr.length()-1) == ',' ){
                // Check if quoteStr is a list of values, which will be indicated by having a "," at the start or end
                final String[] commaSplitRaw = quoteStr.split(",");
                final List<String> commaSplit = removeEmptiesFromArray(commaSplitRaw);
                for(String commaStr : commaSplit){
                    rawValues.add(commaStr);
                }
            } else {
                // quoteStr is a single value, e.g. quoteStr = "32,143"
                rawValues.add(quoteStr);
            }
        }

        List<String> cleanedValues = new ArrayList<>();
        for(String value : rawValues){
            value = value.replace(",", "");
            if(value.contains("%") && value.contains(".")){
                // Percentages need to be converted to their decimal form
                value = value.replace("%", "");
                BigDecimal d = new BigDecimal(value).divide(BigDecimal.valueOf(100));
                value = d.toString();
            }
            cleanedValues.add(value);
        }

        return cleanedValues;
    }
}
