package org.mskcc.cellranger.controller;

import com.codesnippets4all.json.parsers.JSONParser;
import com.codesnippets4all.json.parsers.JsonParserFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.mskcc.cellranger.documentation.CellRangerSummaryCountModel;
import org.mskcc.cellranger.documentation.CellRangerSummaryVDJModel;
import org.mskcc.cellranger.documentation.FieldMapper;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class CellRangerController {
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

    @Value("${cellranger.countdir}")
    private String CELL_RANGER_COUNT_DIR;

    @Value("${cellranger.vdjdir}")
    private String CELL_RANGER_VDJ_DIR;

    @Value("${cellranger.websummarypath}")
    private String WEB_SUMMARY_PATH;

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
    @RequestMapping(value = "/saveCellRangerSample",
            method = RequestMethod.POST,
            produces = "application/json")
    public Map<String, Object> saveCellRangerSample(final HttpServletRequest request) {
        Map jsonMap;
        try {
            jsonMap = getParamMap(request);
        } catch (IOException e) {
            String error = "Failed to parse post body from request";
            log.error(String.format("%s. Error: %s", error, e.getMessage()));
            return createStandardResponse(error, false);
        }

        final String id = (String) jsonMap.get(API_ID);
        final String type = (String) jsonMap.get(API_TYPE);
        final String project = (String) jsonMap.get(API_PROJECT);
        final String run = (String) jsonMap.get(API_RUN);
        if (id == null || type == null || project == null || run == null) {
            final String error = String.format("ERROR: Bad Parameters. Type: %s, Sample: %s, Project: %s, Run: %s",
                    type, id, project, run);
            log.error(error);
            return createStandardResponse(error, false);
        }

        CellRangerDataRecord dataRecord;
        try {
            log.info(String.format("Creating entry for file 'id' %s (Project: %s, Run: %s, Type: %s)", id, project, run, type));
            dataRecord = createDataRecordFromWebSummary(run, id, project, type);
        } catch (IOException | NullPointerException e) {
            final String error = String.format("Failed to read file for w/ name '%s' (Project: '%s', Run: '%s', Type: '%s')", id, project, run, type);
            log.error(String.format("%s. Error: %s", error, e.getMessage()));
            return createStandardResponse(error, false);
        }

        // Save record to database
        CrudRepository repo = getRepository(type);
        String status = String.format("Saved entry for file id '%s' (Project: '%s', Run: '%s', Type: '%s')", id, project, run, type);
        if (repo.existsById(id)) {
            status = String.format("Overwrote entry for id '%s'. %s", id, status);
        }
        repo.save(dataRecord);
        return createStandardResponse(status, true);
    }

    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/getCellRangerSample", method = RequestMethod.GET)
    public Map<String, Object> getCellRangerSample(@RequestParam("project") String project,
                                                   @RequestParam("type") String type) {
        log.info("Querying for CellRangerSample: " + project);

        List<? extends CellRangerDataRecord> samples = getSamples(type, project);
        String status;
        if (samples.isEmpty()) {
            status = String.format("No samples found for %s project '%s'", type, project);
            log.error(status);
            return createStandardResponse(status, false);
        }
        status = String.format("Found %d samples for project '%s'", samples.size(), project);
        Map<String, Object> resp = createStandardResponse(status, true);
        resp.put("data", samples);
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
    private CellRangerDataRecord createDataRecordFromWebSummary(String run, String id, String project, String type) throws IOException {
        // Create Entity w/ id
        final FieldMapperModel fieldMapperModel = getFieldMapperModel(type);
        final CellRangerDataRecord dataRecord = getDataRecord(type);
        dataRecord.setField("id", id, String.class);
        dataRecord.setField("project", project, String.class);
        dataRecord.setField("run", run, String.class);

        // Parse
        final String filePath = getWebSummaryPath(run, id, project, type);
        File input = new File(filePath);
        Document doc = Jsoup.parse(input, "UTF-8", "");

        String value, htmlElement, htmlField;
        Element element;
        Class fieldType;
        List<FieldMapper> fieldMapperList = fieldMapperModel.getFieldMapperList();
        for (FieldMapper fm : fieldMapperList) {
            htmlElement = fm.getHtmlElement();
            htmlField = fm.getHtmlField();
            Elements matches = findMatchesInHtml(doc, htmlElement, htmlField);
            int numMatches = matches.size();
            if (numMatches == 0) {
                log.error(String.format("Field not parsed from CellRanger output. Match not found for %s:%s",
                        htmlElement, htmlField));
                continue;
            }
            for (Element label : matches) {
                element = label.nextElementSibling();  // value follows field in html document
                fieldType = fm.getType();
                value = sanitize(element.text(), fieldType);

                dataRecord.setField(fm.getTableField(), value, fm.getType());
            }
        }

        String compressedGraphData = getCompressedGraphData(doc);
        dataRecord.setField("CompressedGraphData", compressedGraphData, String.class);
        return dataRecord;
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
        return null;
    }

    /**
     * Returns path to where field cell ranger output for input parameters can be found
     */
    private String getWebSummaryPath(String run, String sample, String project, String type) {
        final String baseDir;
        switch (type.toLowerCase()) {
            case API_TYPE_VDJ:
                baseDir = CELL_RANGER_VDJ_DIR;
                break;
            case API_TYPE_COUNT:
                baseDir = CELL_RANGER_COUNT_DIR;
                break;
            default:
                return String.format("ERROR: No corresponding web summary path for %s", type);
        }
        final String samplePath = String.format("/%s/%s/%s", project, run, sample);
        final String runPath = String.format("%s%s/%s", baseDir, samplePath, WEB_SUMMARY_PATH);

        log.info(String.format("Using Web Summary path %s", runPath));
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

    /**
     * Creates a map w/ status and response to be returned to the client
     *
     * @param status,  String - Informative message about the request status
     * @param success, boolean - Whether request succeeded or not
     * @return
     */
    private Map<String, Object> createStandardResponse(String status, boolean success) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", status);
        map.put("success", success ? "true" : "false");

        // Failures can log info that shouldn't be returned to user, e.g. Exception messages/stack traces/etc
        if (success) {
            log.info(status);
        }

        return map;
    }
}
