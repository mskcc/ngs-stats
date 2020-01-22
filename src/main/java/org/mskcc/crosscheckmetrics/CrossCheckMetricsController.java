package org.mskcc.crosscheckmetrics;

import org.mskcc.crosscheckmetrics.model.CrosscheckMetrics;
import org.mskcc.crosscheckmetrics.respository.CrossCheckMetricsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.List;
import java.util.Map;

import static org.mskcc.constants.API_DATA;
import static org.mskcc.utils.ApiUtil.createErrorResponse;
import static org.mskcc.utils.ApiUtil.createSuccessResponse;
import static org.mskcc.utils.ParserUtil.parseFloat;
import static org.mskcc.utils.ParserUtil.parseLine;

@RestController
public class CrossCheckMetricsController {
    private static Logger log = LoggerFactory.getLogger(CrossCheckMetricsController.class);

    @Autowired
    private CrossCheckMetricsRepository crossCheckMetricsRepository;

    @Value("${crosscheckmetrics.dir}")
    private String CROSSCHECK_METRICS_DIR;

    @RequestMapping(value = "/getCrosscheckMetrics", method = RequestMethod.GET)
    // generate the Excel files on demand or via crontab? (can be a bit slow so use crontab?)
    public Map<String, Object> getCrosscheckMetrics(@RequestParam("project") String project) {
        List<CrosscheckMetrics> metrics = crossCheckMetricsRepository.findByProject(project);
        String status;
        if (metrics.isEmpty()) {
            status = String.format("No crosscheckmetrics found for project: '%s'", project);
            return createErrorResponse(status, true);
        }
        status = String.format("Found %d samples for project '%s'", metrics.size(), project);
        Map<String, Object> resp = createSuccessResponse(status);
        resp.put(API_DATA, metrics);
        return resp;
    }

    @RequestMapping(value = "/writeCrosscheckMetrics", method = RequestMethod.GET)
    public Map<String, Object> writeCrosscheckMetrics(@RequestParam("project") String project, @RequestParam("run") String run) {
        // TODO - Find file
        final String filePath = String.format("%s/%s_%s.crosscheck_metrics", CROSSCHECK_METRICS_DIR, project, run);
        try {
            saveCrossCheckMetricsFile(filePath);
        } catch (IOException e){
            log.error(String.format("Failed to read %s: %s", filePath, e.getMessage()));
            return createErrorResponse("", true);
        }
        return createSuccessResponse(String.format("Saved CrossCheckMetrics for Run: %s, Project: %s", run, project));
    }

    /**
     * Writes an entry for each row of the crossCheckMetrics file in the input filePath
     * @param filePath, String - e.g. /PATH/TO/{PROJECT}_{RUN}.crosscheck_metrics
     * @throws IOException
     */
    private void saveCrossCheckMetricsFile(String filePath) throws IOException {
        BufferedReader TSVFile = new BufferedReader(new FileReader(filePath));
        String line = TSVFile.readLine(); // Read first line.
        final List<String> headers = parseLine(line, "\t");

        int lodIndex = getColumnIndex(headers, "lod");
        int resultIndex = getColumnIndex(headers, "result");
        int sampleInfoAIndex = getColumnIndex(headers, "sampleLeft");
        int sampleInfoBIndex = getColumnIndex(headers, "sampleRight");

        Float lod;
        String result;
        String[] sampleInfoA;
        String[] sampleInfoB;
        line = TSVFile.readLine();
        while (line != null){
            List<String> values = parseLine(line, "\t");

            lod = parseFloat(values.get(lodIndex));
            result = values.get(resultIndex);
            sampleInfoA = parseSampleInfoFromBam(values.get(sampleInfoAIndex));
            sampleInfoB = parseSampleInfoFromBam(values.get(sampleInfoBIndex));

            CrosscheckMetrics metrics = new CrosscheckMetrics(lod, result, sampleInfoA, sampleInfoB);
            crossCheckMetricsRepository.save(metrics);

            line = TSVFile.readLine();
        }
        TSVFile.close();
    }

    /**
     * Returns index of a value in an array
     */
    private int getColumnIndex(List<String> headers, String field){
        for(int i = 0; i<headers.size(); i++){
            if(field.equals(headers.get(i))){
                return i;
            }
        }
        log.error("Failed to retrieve field (%s) from headers: %s", field, headers.toString());
        return -1;
    }

    /**
     * Extracts the info from the BAM name used in the picard CrossCheckFingerprint command
     *  TODO - Add examples
     * @param bamName
     */
    private String[] parseSampleInfoFromBam(String bamName){
        String[] sampleInfo = new String[]{"", "", ""};
        List<String> values = parseLine(bamName, "_");

        if(values.size() != 3){
            log.error(String.format("Failed to parse out sampleInfo from bamName: %s", bamName));
            return sampleInfo;
        }
        sampleInfo[0] = values.get(0);
        sampleInfo[1] = values.get(1);
        sampleInfo[2] = values.get(2).replaceAll(".bam","");

        return sampleInfo;
    }
}
