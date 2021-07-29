package org.mskcc.cellranger.controller;

import com.codesnippets4all.json.parsers.JSONParser;
import com.codesnippets4all.json.parsers.JsonParserFactory;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.mskcc.cellranger.documentation.CellRangerSummaryCountModel;
import org.mskcc.cellranger.documentation.CellRangerSummaryVDJModel;
import org.mskcc.cellranger.documentation.FieldMapperModel;
import org.mskcc.cellranger.model.CellRangerDataRecord;
import org.mskcc.cellranger.model.CellRangerSummaryCount;
import org.mskcc.cellranger.model.CellRangerSummaryVdj;
import org.mskcc.cellranger.repository.CellRangerSummaryCountRepository;
import org.mskcc.cellranger.repository.CellRangerSummaryVdjRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.mskcc.Constants.API_DATA;
import static org.mskcc.utils.ApiUtil.createErrorResponse;
import static org.mskcc.utils.ApiUtil.createSuccessResponse;
import static org.mskcc.utils.ParserUtil.parseCellRangerCsvLine;
import static org.mskcc.utils.ParserUtil.readFile;

@RestController
public class CellRangerController {
    final static String API_SAMPLES = "samples";
    final static String API_TYPE_VDJ = "vdj";
    final static String API_TYPE_COUNT = "count";
    final static String API_TYPE = "type";
    final static String API_ID = "sample";
    final static String API_PROJECT = "project";
    final static String API_RUN = "run";

    private static Logger log = LoggerFactory.getLogger(CellRangerController.class);

    @Autowired
    private CellRangerSummaryCountRepository cellRangerSummaryCountRepository;

    @Autowired
    private CellRangerSummaryVdjRepository cellRangerSummaryVdjRepository;

    @Value("${cellranger.dir}")
    private String CELL_RANGER_DIR;

    @Value("${cellranger.websummarypath}")
    private String WEB_SUMMARY_PATH;

    @Value("${cellranger.metricspath}")
    private String METRICS_PATH;

    /**
     * Saves Cell Ranger record to database. Parses file from path determined from input parameters
     * Will save content of sample located at /PATH/TO/count/r1/p1/s1/outs/web_summary.html to DB
     *
     *      Sample Request:
     *      <p>
     *          Input: {
     *              "sample": "s1",
     *              "type": "count",
     *              "project": "p1",
     *              "run": "r1",
     *          }
     *      <p>
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/saveCellRangerSample",
                 produces = "application/json")
    public Map<String, Object> saveCellRangerSample(final HttpServletRequest request) {
        Map jsonMap;
        try {
            jsonMap = getParamMap(request);
        } catch (IOException e) {
            final String error = "Failed to parse post body from request";
            return createErrorResponse(error, true);
        }

        // Check for valid samples parameter
        if( !jsonMap.containsKey(API_SAMPLES) ||
            !(jsonMap.get(API_SAMPLES) instanceof List) ||
            ((List) jsonMap.get(API_SAMPLES)).size() == 0
        ) {
            final String error = String.format("Invalid '%s' parameter. '%s' should be a non-empty list of objects",
                    API_SAMPLES, API_SAMPLES);
            return createErrorResponse(error, true);
        }

        List<Map<String,String>> samples = (List<Map<String,String>>)jsonMap.get(API_SAMPLES);

        // Check that all samples contain all required fields
        List<String> invalidSamples = getInvalidSamples(samples);
        if(invalidSamples.size() > 0){
            final String error = String.format("All samples need to contain fields for %s, %s, %s, & %s. Invalid Samples: %s",
                    API_ID, API_RUN, API_PROJECT, API_TYPE, invalidSamples.toString());
            return createErrorResponse(error, true);
        }

        for(Map<String,String> sample : samples){
            final String id = (String) sample.get(API_ID);
            final String type = (String) sample.get(API_TYPE);
            final String project = (String) sample.get(API_PROJECT);
            final String run = (String) sample.get(API_RUN);

            CellRangerDataRecord dataRecord;
            try {
                log.info(String.format("Creating entry for file 'id' %s (Project: %s, Run: %s, Type: %s)", id, project, run, type));
                dataRecord = createDataRecordFromCellRangerOutput(run, id, project, type);
            } catch (IOException e) {
                final String error = String.format("Failed to read file for w/ name '%s' (Project: '%s', Run: '%s', Type: '%s')", id, project, run, type);
                return createErrorResponse(error, true);
            }
            CrudRepository repo = getRepository(type);
            if (repo.existsById(id)) {
                log.info(String.format("Overwrote entry for id '%s'", id));
            }
            repo.save(dataRecord);
        }
        return createSuccessResponse(String.format("Saved %d Samples", samples.size()));
    }

    /**
     * Returns invalid samples in the list of samples. E.g.
     *      INPUT: [
     *          { ID: A, TYPE: ..., PROJECT: ..., RUN: ... },     // VALID
     *          { ID: B, TYPE: ..., RUN: ... },                   // INVALID
     *      ]
     *
     *      RETURNS: [ 'B' ]
     *
     * @param samples, List of samples represented by java objects
     * @return
     */
    private List<String> getInvalidSamples(List<Map<String, String>> samples) {
        String[] requiredKeys = new String[]{API_TYPE, API_PROJECT, API_RUN};
        List<String> invalidSamples = new ArrayList<>();
        for(Map<String, String> sample : samples){
            if(!sample.containsKey(API_ID)){
                invalidSamples.add(sample.toString());
            } else {
                for(String key : requiredKeys){
                    if(!sample.containsKey(key)){
                        invalidSamples.add(sample.get(API_ID));
                    }
                }
            }
        }

        return invalidSamples;
    }

    @CrossOrigin
    @GetMapping(value = "/getCellRangerSample")
    public Map<String, Object> getCellRangerSample(@RequestParam("project") String project,
                                                   @RequestParam("type") String type) {
        log.info("Querying for CellRangerSample: " + project);

        List<? extends CellRangerDataRecord> samples = getSamples(type, project);
        String status;
        if (samples.isEmpty()) {
            status = String.format("No samples found for %s project '%s'", type, project);
            return createErrorResponse(status, true);
        }
        status = String.format("Found %d samples for project '%s'", samples.size(), project);
        Map<String, Object> resp = createSuccessResponse(status);
        resp.put(API_DATA, samples);
        return resp;
    }

    @CrossOrigin
    @GetMapping(value = "/getCellRangerFile")
    public Map<String, Object> getCellRangerFile( @RequestParam("project") String project,
                                                  @RequestParam("run") String run,
                                                  @RequestParam("sample") String sample,
                                                  @RequestParam("type") String type) {
        log.info(String.format("Retrieving cell ranger output file for run: %s, project: %s, sample: %s, type: %s",
                run, project, sample, type));
        final String webSummaryPath = getCellRangerOutputPath(run, sample, project, type, WEB_SUMMARY_PATH);
        final String data = readFile(webSummaryPath);
        String status = String.format("File not found: %s", webSummaryPath);
        if(data != null){
            status = String.format("Retrieved data from %s", webSummaryPath);
        }
        Map<String, Object> resp = createSuccessResponse(status);
        resp.put(API_DATA, data);
        return resp;
    }

    /**
     * Reads Post Body from request object
     * REF - https://stackoverflow.com/a/14885950/3874247
     *
     * @param request
     * @return String
     * @throws IOException
     */
    private static String getBody(HttpServletRequest request) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;

        try {
            InputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } else {
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    throw ex;
                }
            }
        }
        return stringBuilder.toString();
    }

    /**
     * Returns Map of Request's Post Body. Supports nested fields
     *
     * @param request
     * @return Map<String, String>
     * @throws IOException
     */
    private Map getParamMap(HttpServletRequest request) throws IOException {
        JSONParser parser = JsonParserFactory.getInstance().newJsonParser();
        String payloadRequest = getBody(request);

        return parser.parseJson(payloadRequest);
    }

    /**
     * Parses data from web_summary.html and creates a CellRangerDataRecord instance. The fields it parses is
     * dependent upon the parameters passed to it, specifically the "type", which can be either vdj or count.
     *
     * @param run,     String - Run of the sample
     * @param id,      String - Name of file containing the sample
     * @param project, String - Project of the sample
     * @param type,    String - cellranger output type. Must have an associated model in org.mskcc.cellranger.model
     * @throws IOException
     * @return, CellRangerDataRecord - Field populated from web_summary file
     */
    private CellRangerDataRecord createDataRecordFromCellRangerOutput(String run, String id, String project, String type) throws IOException {
        // Create Entity w/ id
        final FieldMapperModel fieldMapperModel = getFieldMapperModel(type);
        final CellRangerDataRecord dataRecord = getDataRecord(type);
        dataRecord.setField("id", id, String.class);
        dataRecord.setField("project", getProjectId(project), String.class);
        dataRecord.setField("run", run, String.class);

        // Extract Statistics from the "metrics_summary.csv"
        final String metricsPath = getCellRangerOutputPath(run, id, project, type, METRICS_PATH);
        try (BufferedReader br = new BufferedReader(new FileReader(metricsPath))) {
            String header = br.readLine();
            List<String> headerValues = parseCellRangerCsvLine(header);

            List<String> values;
            String line, field, headerVal, sqlColumn;
            Class sqlType;
            while ((line = br.readLine()) != null) {
                values = parseCellRangerCsvLine(line);
                if (values.size() == headerValues.size()) {
                    for (int i = 0; i < values.size(); i++) {
                        headerVal = headerValues.get(i);
                        field = values.get(i);
                        sqlColumn = fieldMapperModel.getSqlColumn(headerVal);
                        sqlType = fieldMapperModel.getSqlType(headerVal);
                        if(sqlColumn != null && sqlType != null){
                            dataRecord.setField(sqlColumn, field, sqlType);
                        } else {
                            log.error(String.format("Failed to extract header value: %s (sqlColumn: %s, sqlType: %s)",
                                    headerVal, sqlColumn, sqlType));
                        }
                    }
                } else {
                    log.error(String.format("Number Headers (%d) don't match Number Values (%d): %s",
                            headerValues.length, values.size(), metricsPath));
                }
            }
        }
        return dataRecord;
    }

    /**
     * Remove the "Project_" prefix if it exists
     * @param projectParam, String -            e.g. "Project_09666H"   "09666H"
     *                                                      |               |
     * @return Processed Param                          "09666H"        "09666H"
     */
    private String getProjectId(String projectParam){
        String[] prjSplit = projectParam.split("Project_", 2);
        if(prjSplit.length > 1){
            return prjSplit[1];
        }
        return projectParam;
    }

    /**
     * Get Graph Data, which has different parsing logic. Looks for <script/> tag, which should contain compressed graph
     * data in single-quotes
     *      e.g. Should grab, COMPRESSED_DATA, from the html doc excerpt
     *          <script type="text/javascript">
     *              var compressed_data = '[COMPRESSED_DATA]'
     *              ...
     *          </script>
     *
     * @param doc
     * @return
     */
    private String getCompressedGraphData(Document doc) {
        String identifier = String.format("script[type='text/javascript']");
        Elements graphData = doc.select(identifier);
        String contents;
        for (Element match : graphData) {
            contents = match.data();
            Pattern pattern = Pattern.compile("'(.*?)'");
            Matcher matcher = pattern.matcher(contents);
            if (matcher.find()) {
                return matcher.group(1);
            }
        }
        log.error("Could not extract graph data");
        return "";
    }

    /**
     * Returns all Document elements that match
     *
     * @param doc,         Document         - Representation of Html Document
     * @param htmlElement, String   - DOM element, e.g. "h1"/"td"
     * @param htmlField,   String     - Label of the neighboring DOM element that neighbors target value
     * @return
     */
    private Elements findMatchesInHtml(Document doc, String htmlElement, String htmlField) {
        String identifier = String.format("%s:contains(%s)", htmlElement, htmlField);
        return doc.select(identifier);
    }

    private List<? extends CellRangerDataRecord> getSamples(String type, String project) {
        switch (type.toLowerCase()) {
            case API_TYPE_VDJ:
                return cellRangerSummaryVdjRepository.findByProject(project);
            case API_TYPE_COUNT:
                return cellRangerSummaryCountRepository.findByProject(project);
            default:
                break;
        }
        return new ArrayList();
    }


    /**
     * Returns the CellRangerDataRecord model that will be saved directly to the database
     *
     * @param type
     * @return
     */
    private CellRangerDataRecord getDataRecord(String type) {
        switch (type.toLowerCase()) {
            case API_TYPE_VDJ:
                return new CellRangerSummaryVdj();
            case API_TYPE_COUNT:
                return new CellRangerSummaryCount();
            default:
                break;
        }
        log.error(String.format("Invalid Type for Data Record: %s", type));
        return null;
    }


    private CrudRepository getRepository(String type) {
        switch (type.toLowerCase()) {
            case API_TYPE_VDJ:
                return cellRangerSummaryVdjRepository;
            case API_TYPE_COUNT:
                return cellRangerSummaryCountRepository;
            default:
                break;
        }
        return null;
    }

    /**
     * Returns Model w/ mappings for parsing cell-ranger output and creating the dataRecord field
     *
     * @param type
     * @return, FieldMapperModel
     */
    private FieldMapperModel getFieldMapperModel(String type) {
        switch (type.toLowerCase()) {
            case API_TYPE_VDJ:
                return new CellRangerSummaryVDJModel();
            case API_TYPE_COUNT:
                return new CellRangerSummaryCountModel();
            default:
                break;
        }
        log.error(String.format("Invalid Type for FieldMapper: %s", type));
        return null;
    }

    /**
     * Returns path to where field cell ranger output for input parameters can be found
     */
    private String getCellRangerOutputPath(String run, String sample, String project, String type, String path) {
        final String samplePath = String.format("/%s/%s/%s__%s", run, project, sample, type.toLowerCase());
        final String runPath = String.format("%s%s/%s", CELL_RANGER_DIR, samplePath, path);
        log.info(String.format("Cell Ranger Output path: %s", runPath));
        return runPath;
    }

    /**
     * Transforms string input into string that can be cast to the input Type
     * e.g.
     * "97.0%"  -> "0.97"
     * "19,431" -> "19431"
     * "GRCh38" -> "GRCh38"    (no change)
     *
     * @param input, String - Input that needs to be transformed
     * @param type
     * @return
     */
    private String sanitize(String input, Class type) {
        if (input == null) return "";
        String value = input.trim();

        // String values do not need to be formatted
        if (String.class.toString().equals(type.toString())) {
            return value;
        }
        return sanitizeNumber(value);
    }

    /**
     * Sanitizes string of a number for any characters that won't be parsable
     * e.g.     97.0%  -> 0.97
     * 19,431 -> 19431
     *
     * @param value
     * @return
     */
    private String sanitizeNumber(String value) {
        if (isPercent(value)) {
            return formatPercent(value);
        }
        return value.replace(",", "");
    }

    private boolean isPercent(String input) {
        return input.indexOf("%") != -1;
    }

    private String formatPercent(String percentage) {
        BigDecimal bd = new BigDecimal(percentage.replace("%", "")).divide(BigDecimal.valueOf(100));
        return bd.toString();
    }


}
