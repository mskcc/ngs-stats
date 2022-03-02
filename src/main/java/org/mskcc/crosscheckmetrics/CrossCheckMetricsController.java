package org.mskcc.crosscheckmetrics;

import org.mskcc.crosscheckmetrics.model.CrosscheckMetrics;
import org.mskcc.crosscheckmetrics.model.ProjectEntries;
import org.mskcc.crosscheckmetrics.model.SampleInfo;
import org.mskcc.crosscheckmetrics.respository.CrossCheckMetricsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static org.mskcc.Constants.*;
import static org.mskcc.utils.ApiUtil.createErrorResponse;
import static org.mskcc.utils.ApiUtil.createSuccessResponse;
import static org.mskcc.utils.ParserUtil.parseDouble;

@RestController
public class CrossCheckMetricsController {
    private static final String RESULT = "RESULT";
    private static final String LEFT_FILE = "LEFT_FILE";
    private static final String RIGHT_FILE = "RIGHT_FILE";
    private static Logger log = LoggerFactory.getLogger(CrossCheckMetricsController.class);
    private final String LAST_METADATA_LINE = "## METRICS CLASS";
    private final String LOD_SCORE = "LOD_SCORE";
    @Autowired
    private CrossCheckMetricsRepository crossCheckMetricsRepository;

    @Value("${crosscheckmetrics.dir}")
    private String [] CROSSCHECK_METRICS_DIRS;

    @CrossOrigin
    @RequestMapping(value = "/getCrosscheckMetrics", method = RequestMethod.GET)
    public Map<String, Object> getCrosscheckMetrics(@RequestParam("projects") String projects) {
        List<String> projectList = Arrays.asList(projects.split(","));
        List<CrosscheckMetrics> results = crossCheckMetricsRepository.findByCrosscheckMetrics_IdProject_IsIn(projectList);
        log.info("/getCrosscheckMetrics");
        log.info(String.format("Projects: %s", projects));
        String status;
        if (results.isEmpty()) {
            status = String.format("No crosscheckmetrics found for projects: '%s'", projects);
            return createErrorResponse(status, true);
        }

        // Create API response for each DB entry
        final Map<String, ProjectEntries> response = new HashMap<>();
        for(CrosscheckMetrics entry : results){
            String project = entry.getCrosscheckMetricsId().getProject();
            if(response.containsKey(project)){
                response.get(project).addEntry(entry);
            } else {
                response.put(project, new ProjectEntries(project, entry));
            }
        }

        status = String.format("Found %d samples for %d projects '%s'", results.size(), response.size(), projects);
        Map<String, Object> resp = createSuccessResponse(status, results);
        resp.put(API_DATA, response);
        return resp;
    }

    /**
     * Given the project and run that crosscheckmetrics has been saved for, locate the .crosscheck_metrics file on the
     * filesystem and save its values to the DB
     *
     * @param project, e.g. "P10439_B"
     * @return, API response
     */
    @RequestMapping(value = "/writeCrosscheckMetrics", method = RequestMethod.GET)
    public Map<String, Object> writeCrosscheckMetrics(@RequestParam("project") String project) {
        // Crosscheck metrics are stored on the filesystem ngs-stats runs on
        List<CrosscheckMetrics> crosscheckMetricsObjects = new LinkedList<>();
        final String fileName = String.format("%s.crosscheck_metrics", project);
        int crosscheckMetricObjsIndex = 0;
        for (String crosscheckMetricsDir : CROSSCHECK_METRICS_DIRS) {
            final String filePath = String.format("%s/%s/%s", crosscheckMetricsDir, project, fileName);
            if (!new File(filePath).exists()) {
                log.info("Skipping non-existent file: " + filePath);
                continue;
            }
            log.info(String.format("/writeCrosscheckMetrics: Reading %s", fileName));

            try {
                crosscheckMetricsObjects = saveCrossCheckMetricsFile(filePath);
            } catch (IOException | IllegalStateException e) {
                String status = String.format("Failed to read %s: %s", filePath, e.getMessage());
                return createErrorResponse(status, false);
            }
            final String status = String.format("Saved CrossCheckMetrics for Project: %s", project);
            return createSuccessResponse(status, crosscheckMetricsObjects.get(crosscheckMetricObjsIndex++));
        }
        return createErrorResponse("No file found", true);
    }

    /**
     * Writes an entry for each row of the crossCheckMetrics file in the input filePath
     *
     * @param filePath, String - e.g. /PATH/TO/{PROJECT}_{RUN}.crosscheck_metrics
     * @throws IOException
     */
    private List<CrosscheckMetrics> saveCrossCheckMetricsFile(String filePath) throws IOException, IllegalStateException {
        List<CrosscheckMetrics> crosscheckMetricObjs = new LinkedList<>();
        BufferedReader TSVFile = new BufferedReader(new FileReader(filePath));
        String line = TSVFile.readLine(); // Read first line.

        // Skip Metadata lines
        boolean readingMetadata = true;
        while (readingMetadata && line != null) {
            String[] metadataValues = line.split(TAB);
            if (metadataValues.length > 0 && metadataValues[0].toUpperCase().equals(LAST_METADATA_LINE)) {
                readingMetadata = false;
            } else {
                line = TSVFile.readLine();
            }
        }

        // Parse columns from next header line
        line = TSVFile.readLine();

        if (line != null) {
            final String[] headers = line.split(TAB);
            int lodIndex = getColumnIndex(headers, LOD_SCORE);
            int resultIndex = getColumnIndex(headers, RESULT);
            int sampleInfoAIndex = getColumnIndex(headers, LEFT_FILE);
            int sampleInfoBIndex = getColumnIndex(headers, RIGHT_FILE);

            line = TSVFile.readLine();
            while (line != null) {
                crosscheckMetricObjs.add(createAndReturnCrosscheckMetricsEntry(line, lodIndex, resultIndex, sampleInfoAIndex, sampleInfoBIndex));
                line = TSVFile.readLine();
            }
        } else {
            final String status = String.format("Failed to find entries in %s", filePath);
            throw new IllegalStateException(status);
        }
        TSVFile.close();
        return crosscheckMetricObjs;
    }

    /**
     * Parses CrosscheckMetrics info from line and saves to database
     *
     * @param line,             String - Line from cross_check metrics file containing values
     * @param lodIndex,         int - Index of delimited line w/ lodIndex information
     * @param resultIndex,      int - Index of results
     * @param sampleInfoAIndex, int - Index of first sample
     * @param sampleInfoBIndex, int - Index of second sample
     */
    private CrosscheckMetrics createAndReturnCrosscheckMetricsEntry(String line, int lodIndex, int resultIndex, int sampleInfoAIndex, int sampleInfoBIndex) throws IllegalStateException {
        if (line.equals("")) return null;
        final String[] values = line.split(TAB);

        int[] indices = new int[]{lodIndex + 2, resultIndex, sampleInfoAIndex, sampleInfoBIndex};
        for (int idx : indices) {
            if (values.length <= idx) {
                String status = String.format("Entry not saved. Could not parse from line: %s", line);
                throw new IllegalStateException(status);
            }
        }

        // LOD columns are together, the first one being named "LOD_SCORE"
        // Example Headers: "...DATA_TYPE   LOD_SCORE   LOD_SCORE_TUMOR_NORMAL  LOD_SCORE_NORMAL_TUMOR..."
        final Double lod = parseDouble(values[lodIndex]);
        final Double lodAlt1 = parseDouble(values[lodIndex + 1]);
        final Double lodAlt2 = parseDouble(values[lodIndex + 2]);
        final String result = values[resultIndex];
        final String pathA = values[sampleInfoAIndex];
        final String pathB = values[sampleInfoBIndex];

        // PATH -> [ PROJECT_ID, IGO_ID, PATIENT_ID, TUMOR/NORMAL ] OR [ PATIENT_ID, PROJECT_ID, IGO_ID ]
        final SampleInfo sampleInfoA = getSampleInfo(pathA);
        final SampleInfo sampleInfoB = getSampleInfo(pathB);

        String project = sampleInfoA.getProject();
        if (!project.equals(sampleInfoB.getProject())) {
            String differentProject = sampleInfoB.getProject();
            log.error("Detected samples from two different projects: %s & %s. Saving entry with project: %s_%s",
                    project, differentProject, project, differentProject);
            project = String.format("%s__%s", project, differentProject);
        }

        // Save to database
        CrosscheckMetrics metrics = new CrosscheckMetrics(lod, lodAlt1, lodAlt2, project, result, sampleInfoA, sampleInfoB);
        crossCheckMetricsRepository.save(metrics);
        return metrics;
    }

    /**
     * Extracts the info from the BAM name used in the picard CrossCheckFingerprint command
     * {BAM_NAME} -> [ PROJECT_ID, IGO_ID, PATIENT_ID, TUMOR/NORMAL ]
     *  e.g.
     *      PATH/TO/P04969_N__09455_S_1__C-7JJ452__Tumor_headers.bam -> ["P04969_N", "09455_S_1", "C-7JJ452", "Tumor"]
     *
     * @param pathName, e.g. "PATH/TO/09455_S_1__C-7JJ452__Tumor_headers.bam"
     */
    private SampleInfo getSampleInfo(String pathName) {
        String fileName = getFileName(pathName);
        String[] values = fileName.split(BAM_DELIMITER);

        if (values.length == 3) {
            values[2] = values[2].replaceAll(".vcf*", "");
            return new SampleInfo(values[0], values[1], values[2]);
        }
        else if (values.length == 4) {
            values[3] = values[3].replaceAll(".bam", "")
                    .replaceAll("_headers", "");
            return new SampleInfo(values[0], values[1], values[2], values[3]);
        }
        else {
            log.error(String.format("Failed to parse out sampleInfo from bam or vcf name: %s", fileName));
            return new SampleInfo("", "", "", "");
        }
    }

    /**
     * Retrieves BAM name from the input path name
     *
     * @param pathName, e.g. "PATH/TO/P04969_N__04969_N_5__C-000238__Tumor_headers.bam"
     * @return, e.g. "P04969_N__04969_N_5__C-000238__Tumor_headers.bam"
     */
    private String getFileName(String pathName) {
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
