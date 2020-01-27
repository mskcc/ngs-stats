package org.mskcc.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
}
