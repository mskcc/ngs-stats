package org.mskcc.utils;

import org.mskcc.crosscheckmetrics.model.CrosscheckMetrics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class ApiUtil {
    private static Logger log = LoggerFactory.getLogger(ApiUtil.class);

    /**
     * Creates a map w/ status and response to be returned to the client. Includes logging
     *
     * @param status, String - Informative message about the request status
     * @return
     */
    public static Map<String, Object> createSuccessResponse(String status, Object crosscheckMetrics) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", status);
        map.put("success", "true");
        map.put("crosscheckMetrics", crosscheckMetrics);
        log.info(status);
        return map;
    }

    /**
     * Creates a map w/ status and error response to be returned to the client. Includes logging
     *
     * @param status,         String - Message to log
     * @param returnToClient, boolean - Whether the status should be returned to client
     * @return
     */
    public static Map<String, Object> createErrorResponse(String status, boolean returnToClient) {
        Map<String, Object> map = new HashMap<>();
        map.put("success", "false");
        log.error(status);

        if (returnToClient) {
            map.put("status", status);
        } else {
            map.put("status", "Server Error. Email skigodata@mskcc.org");
        }

        return map;
    }
}
