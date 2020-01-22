package org.mskcc.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class ParserUtil {
    private static Logger log = LoggerFactory.getLogger(ParserUtil.class);

    /**
     * Safe conversion of a string to a float with logging
     */
    public static Float parseFloat(String st){
        try {
            Float ft = Float.parseFloat(st);
            return ft;
        } catch(NumberFormatException e) {
            log.error(String.format("Failed to convert %s to float: %s",st, e.getMessage()));
        }
        return null;
    }

    /**
     * Return a list of the delimited values in a string
     *
     * @param dataRow, String
     * @return
     */
    public static List<String> parseLine(String dataRow, String delimiter){
        StringTokenizer st = new StringTokenizer(dataRow,delimiter);
        List<String> dataArray = new ArrayList<>() ;
        while(st.hasMoreElements()){
            dataArray.add(st.nextElement().toString());
        }
        return dataArray;
    }
}
