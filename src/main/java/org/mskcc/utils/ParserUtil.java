package org.mskcc.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

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
        // SPECIAL CHARACTERS - These enclose values of the line
        Character COMMA_CHAR = ',';     // .., 0.69, ...
        Character QUOTE_CHAR = '"';     // ..., "1,053", ...

        List<String> values = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        Stack<Character> quoteStack = new Stack<>();    // Pushes/pops " char; when non-empty, is reading quoted value
        for(int i = 0; i<line.length(); i++){
            Character c = line.charAt(i);
            if(QUOTE_CHAR.equals(c)){
                if(quoteStack.size() > 0){
                    // Closing QUOTE_CHAR encountered - end state of reading in value, add string value, and clear stack
                    quoteStack.pop();
                    values.add(sb.toString());
                    sb = new StringBuilder();
                } else {
                    // Opening QUOTE_CHAR encountered - enter state of reading value until another QUOTE_CHAR is read
                    quoteStack.push(c);
                }
            } else if(COMMA_CHAR.equals(c)){
                if(quoteStack.size() > 0){
                    // In state of reading in opened-quote string value, e.g. reading the ',' of "1,053"
                    sb.append(c);
                } else if (sb.length() == 0) {
                    // Pass - A COMMA_CHAR is never the leading character of a value; value must have just been added
                } else {
                    // Otherwise, this delimits COMMA_CHAR the current string from the next
                    values.add(sb.toString());
                    sb = new StringBuilder();
                }
            } else {
                // Not a special character - continue building the string
                sb.append(c);
            }
        }
        // Add last parsed value
        if(sb.length() > 0){
            values.add(sb.toString());
        }

        // Clean up the values, i.e. remove: , %
        List<String> cleanedValues = new ArrayList<>();
        for(String value : values){
            value = value.replace(",", "");
            value = value.replace("\"", "");

            if(value.contains(".")){
                BigDecimal d = new BigDecimal(0);
                if(value.contains("%")){
                    value = value.replace("%", "");
                    d = new BigDecimal(value).divide(BigDecimal.valueOf(100));
                } else {
                    d = new BigDecimal(value);
                }
                if(! (d.compareTo(new BigDecimal(1)) > 1) ){
                    // If the value is a decimal (less than or equal to 1), make the double the value
                    value = d.toString();
                }
            }
            cleanedValues.add(value);
        }

        return cleanedValues;
    }

    /**
     * Reads the file at the input path into a string
     * @param path
     * @return
     */
    public static String readFile(String path) {
        String strLine = "";
        String str_data = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            while (strLine != null)
            {
                if (strLine == null)
                    break;
                str_data += strLine;
                strLine = br.readLine();

            }
            br.close();
            return str_data;
        } catch (FileNotFoundException e) {
            System.err.println(String.format("File not found: %s", path));
        } catch (IOException e) {
            System.err.println(String.format("Unable to read the file: %s", path));
        }
        return null;
    }
}