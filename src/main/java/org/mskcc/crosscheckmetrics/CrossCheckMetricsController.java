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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.mskcc.constants.*;
import static org.mskcc.utils.ApiUtil.createErrorResponse;
import static org.mskcc.utils.ApiUtil.createSuccessResponse;
import static org.mskcc.utils.ParserUtil.parseDouble;

@RestController
public class CrossCheckMetricsController {
    private static Logger log = LoggerFactory.getLogger(CrossCheckMetricsController.class);

    @Autowired
    private CrossCheckMetricsRepository crossCheckMetricsRepository;

    @Value("${crosscheckmetrics.dir}")
    private String CROSSCHECK_METRICS_DIR;

    @RequestMapping(value = "/getCrosscheckMetrics", method = RequestMethod.GET)
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

    /**
     * Given the project and run that crosscheckmetrics has been saved for, locate the .crosscheck_metrics file on the
     * filesystem and save its values to the DB
     *
     * @param project, e.g. "P10439_B"
     * @param run,     e.g. "PITT_0439_BHFTCNBBXY"
     * @return, API response
     */
    @RequestMapping(value = "/writeCrosscheckMetrics", method = RequestMethod.GET)
    public Map<String, Object> writeCrosscheckMetrics(@RequestParam("project") String project, @RequestParam("run") String run) {
        // Crosscheck metrics are stored on the filesystem ngs-stats runs on
        final String fileName = String.format("%s_%s.crosscheck_metrics", project, run);
        final String filePath = String.format("%s/%s/%s/%s", CROSSCHECK_METRICS_DIR, run, project, fileName);
        try {
            saveCrossCheckMetricsFile(filePath);
        } catch (IOException e) {
            String status = String.format("Failed to read %s: %s", filePath, e.getMessage());

            log.error(status);
            return createErrorResponse(status, true);
        }
        return createSuccessResponse(String.format("Saved CrossCheckMetrics for Run: %s, Project: %s", run, project));
    }

    /**
     * Writes an entry for each row of the crossCheckMetrics file in the input filePath
     *
     * @param filePath, String - e.g. /PATH/TO/{PROJECT}_{RUN}.crosscheck_metrics
     * @throws IOException
     */
    private void saveCrossCheckMetricsFile(String filePath) throws IOException {
        BufferedReader TSVFile = new BufferedReader(new FileReader(filePath));
        String line = TSVFile.readLine(); // Read first line.

        // Skip all irrelevant header lines
        boolean readingMetadata = true;
        while (readingMetadata && line != null) {
            String[] metadataValues = line.split(TAB);
            if (metadataValues.length > 0 && metadataValues[0].toUpperCase().equals(LAST_METADATA_LINE)) {
                readingMetadata = false;
            } else {
                line = TSVFile.readLine();
            }
        }

        line = TSVFile.readLine();
        final String[] headers = line.split(TAB);
        int lodIndex = getColumnIndex(headers, LOD_SCORE);
        int resultIndex = getColumnIndex(headers, RESULT);
        int sampleInfoAIndex = getColumnIndex(headers, LEFT_FILE);
        int sampleInfoBIndex = getColumnIndex(headers, RIGHT_FILE);

        line = TSVFile.readLine();
        while (line != null) {
            createCrosscheckMetricsEntry(line, lodIndex, resultIndex, sampleInfoAIndex, sampleInfoBIndex);
            line = TSVFile.readLine();
        }
        TSVFile.close();
    }

    private void createCrosscheckMetricsEntry(String line, int lodIndex, int resultIndex, int sampleInfoAIndex, int sampleInfoBIndex) {
        if (line.equals("")) return;

        final String[] values = line.split(TAB);

        // LOD columns are together, the first one being named "LOD_SCORE"
        // Example Headers: "...DATA_TYPE   LOD_SCORE   LOD_SCORE_TUMOR_NORMAL  LOD_SCORE_NORMAL_TUMOR..."
        final Double lod = parseDouble(values[lodIndex]);
        final Double lodAlt1 = parseDouble(values[lodIndex + 1]);
        final Double lodAlt2 = parseDouble(values[lodIndex + 2]);
        final String result = values[resultIndex];
        final String pathA = values[sampleInfoAIndex];
        final String pathB = values[sampleInfoBIndex];

        // PATH -> [ PROJECT_ID, IGO_ID, PATIENT_ID, TUMOR/NORMAL ]
        final String[] sampleInfoA = getSampleInfo(pathA);
        final String[] sampleInfoB = getSampleInfo(pathB);

        String project = sampleInfoA[0];
        if (!sampleInfoA[0].equals(sampleInfoB[0])) {
            log.error("Detected samples from two different projects: %s & %s. Saving entry with project: %s_%s",
                    sampleInfoA[0], sampleInfoB[0], sampleInfoA[0], sampleInfoB[0]);
            project = String.format("%s__%s", sampleInfoA[0], sampleInfoB[0]);
        }

        // Save to databse
        CrosscheckMetrics metrics = new CrosscheckMetrics(lod, lodAlt1, lodAlt2, project, result, sampleInfoA, sampleInfoB);
        crossCheckMetricsRepository.save(metrics);
    }

    /**
     * Extracts the info from the BAM name used in the picard CrossCheckFingerprint command
     * {BAM_NAME} -> [ PROJECT_ID, IGO_ID, PATIENT_ID, TUMOR/NORMAL ]
     * e.g.
     * PATH/TO/P04969_N__09455_S_1__C-7JJ452__Tumor_headers.bam -> ["P04969_N", "09455_S_1", "C-7JJ452", "Tumor"]
     *
     * @param pathName, e.g. "PATH/TO/09455_S_1__C-7JJ452__Tumor_headers.bam"
     */
    private String[] getSampleInfo(String pathName) {
        String bamName = getBamName(pathName);
        String[] values = bamName.split(BAM_DELIMITER);
        if (values.length != 4) {
            log.error(String.format("Failed to parse out sampleInfo from bamName: %s", bamName));
            return new String[]{"", "", "", ""};
        }
        values[3] = values[3].replaceAll(".bam", "")
                             .replaceAll("_headers", "");
        return values;
    }

    /**
     * Retrieves BAM name from the input path name
     *
     * @param pathName, e.g. "PATH/TO/P04969_N__04969_N_5__C-000238__Tumor_headers.bam"
     * @return,         e.g. "P04969_N__04969_N_5__C-000238__Tumor_headers.bam"
     */
    private String getBamName(String pathName) {
        String[] values = pathName.split(BACKSLASH);
        if (values.length == 0) {
            log.error(String.format("Failed to parse path: %s", pathName));
            return "";
        }
        return values[values.length - 1];
    }

    /**
     * Returns index of a value in an array w/ logging
     */
    private int getColumnIndex(String[] headers, String field) {
        for (int i = 0; i < headers.length; i++) {
            if (field.equals(headers[i])) {
                return i;
            }
        }
        log.error("Failed to retrieve field (%s) from headers: %s", field, headers.toString());
        return -1;
    }
}
