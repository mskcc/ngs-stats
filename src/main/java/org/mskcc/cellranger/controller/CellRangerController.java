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
import org.mskcc.cellranger.model.CellRangerSummaryCount;
import org.mskcc.cellranger.model.CellRangerSummaryVdj;
import org.mskcc.cellranger.model.CellRangerDataRecord;
import org.mskcc.cellranger.repository.CellRangerSummaryCountRepository;
import org.mskcc.cellranger.repository.CellRangerSummaryVdjRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class CellRangerController {
    final String API_TYPE_VDJ = "vdj";
    final String API_TYPE_COUNT = "count";
    final String API_TYPE = "type";
    final String API_SAMPLE = "sample";
    final String API_PROJECT = "project";
    final String API_RUN = "run";

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
     * Returns Map of Request's Post Body. Supports nested fields
     *
     * @param request
     * @return Map<String,String>
     * @throws IOException
     */
    private Map getParamMap(HttpServletRequest request) throws IOException {
        JSONParser parser = JsonParserFactory.getInstance().newJsonParser();
        String payloadRequest = getBody(request);

        return parser.parseJson(payloadRequest);
    }

    /**
     * Saves Cell Ranger record to database. Parses file from path determined from input parameters
     *
     *  Sample Request,
     *      Input: {
     * 	        "sample": "s1",
     * 	        "type": "count",
     * 	        "project": "p1",
     * 	        "run": "r1",
     *      }
     *
     *      Will save content of sample located at /PATH/TO/count/r1/p1/s1/outs/web_summary.html to DB
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/saveCellRangerSample",
                    method = RequestMethod.POST,
                    produces = "application/json")
    public Map<String,String> saveCellRangerSample(final HttpServletRequest request) {
        Map jsonMap;
        try{
            jsonMap = getParamMap(request);
        } catch(IOException e){
            String error = "Failed to parse post body from request";
            log.error(String.format("%s. Error: %s", error, e.getMessage()));
            return mapResponse(error, false);
        }

        final String sample = (String) jsonMap.get(API_SAMPLE);
        final String type = (String) jsonMap.get(API_TYPE);
        final String project = (String) jsonMap.get(API_PROJECT);
        final String run = (String) jsonMap.get(API_RUN);
        if( sample == null || type == null || project == null || run == null){
            final String error = String.format("ERROR: Bad Parameters. Type: %s, Sample: %s, Project: %s, Run: %s",
                    type, sample, project, run);
            log.error(error);
            return mapResponse(error, false);
        }

        CellRangerDataRecord dataRecord;
        try {
            dataRecord = populateEntity(run, sample, project, type);
        } catch(IOException e){
            final String error = String.format("Failed to read file for sample %s", sample);
            log.error(String.format("%s. Error: %s", error, e.getMessage()));
            return mapResponse(error, false);
        }
        putRecordIntoDB(dataRecord, type);

        return mapResponse(String.format("Created entry for Type: %s, Sample: %s, Project: %s, Run: %s",
                type, sample, project, run), true);
    }

    /**
     * Reads Post Body from request object
     *      REF - https://stackoverflow.com/a/14885950/3874247
     *
     * @param request
     * @return String
     * @throws IOException
     */
    private static String getBody(HttpServletRequest request) throws IOException {
        String body = null;
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
                stringBuilder.append("");
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

        body = stringBuilder.toString();
        return body;
    }

    private CellRangerDataRecord populateEntity(String run, String sample, String project, String type) throws IOException {
        // Create Entity w/ sample-id
        final FieldMapperModel fieldMapperModel = getFieldMapperModel(type);
        final CellRangerDataRecord dataRecord = getDataRecord(type);
        dataRecord.setField("id", sample, String.class);

        // Parse
        final String filePath = getWebSummaryPath(run, sample, project, type);
        File input = new File(filePath);
        Document doc = Jsoup.parse(input, "UTF-8", ""); // TODO - throws warning

        String value, htmlElement, htmlField;
        Element element;
        Class fieldType;
        List<FieldMapper> fieldMapperList = fieldMapperModel.getFieldMapperList();
        for(FieldMapper fm : fieldMapperList){
            htmlElement = fm.getHtmlElement();
            htmlField = fm.getHtmlField();
            Elements matches = findMatchesInHtml(doc, htmlElement, htmlField);
            int numMatches = matches.size();
            if(numMatches == 0){
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

        return dataRecord;
    }

    /**
     * Returns all Document elements that match
     *
     * @param doc, Document         - Representation of Html Document
     * @param htmlElement, String   - DOM element, e.g. "h1"/"td"
     * @param htmlField, String     - Label of the neighboring DOM element that neighbors target value
     * @return
     */
    private Elements findMatchesInHtml(Document doc, String htmlElement, String htmlField){
        String identifier = String.format("%s:contains(%s)",htmlElement, htmlField);
        return doc.select(identifier);
    }

    private void putRecordIntoDB(CellRangerDataRecord dataRecord, String type){
        switch (type.toLowerCase()) {
            case API_TYPE_VDJ:
                CellRangerSummaryVdj cellRangerSummaryVdj = (CellRangerSummaryVdj) dataRecord;
                cellRangerSummaryVdjRepository.save(cellRangerSummaryVdj);
            case API_TYPE_COUNT:
                CellRangerSummaryCount cellRangerSummaryCountModel = (CellRangerSummaryCount) dataRecord;
                cellRangerSummaryCountRepository.save(cellRangerSummaryCountModel);
            default:
                break;
        }
    }

    /**
     * Returns the CellRangerDataRecord model that will be saved directly to the database
     *
     * @param type
     * @return
     */
    private CellRangerDataRecord getDataRecord(String type){
        switch (type.toLowerCase()) {
            case API_TYPE_VDJ:
                return new CellRangerSummaryCount();
            case API_TYPE_COUNT:
                return new CellRangerSummaryCount();
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
    private FieldMapperModel getFieldMapperModel(String type){
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
    private String getWebSummaryPath(String run, String sample, String project, String type){
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
        final String runPath = String.format("%s%s%s", baseDir, samplePath, WEB_SUMMARY_PATH);

        log.info(String.format("Using path %s", runPath));
        return runPath;
    }

    /**
     * Transforms string input into string that can be cast to the input Type
     *      e.g.
     *          "97.0%"  -> "0.97"
     *          "19,431" -> "19431"
     *          "GRCh38" -> "GRCh38"    (no change)
     *
     * @param input, String - Input that needs to be transformed
     * @param type
     * @return
     */
    private String sanitize(String input, Class type){
        if(input == null) return "";
        String value = input.trim();

        // String values do not need to be formatted
        if(String.class.toString().equals(type.toString())){
            return value;
        }
        return sanitizeNumber(value);
    }

    /**
     * Sanitizes string of a number for any characters that won't be parsable
     *      e.g.     97.0%  -> 0.97
     *               19,431 -> 19431
     * @param value
     * @return
     */
    private String sanitizeNumber(String value){
        if(isPercent(value)){
            return formatPercent(value);
        }
        return value.replace(",", "");
    }

    private boolean isPercent(String input){
        return input.indexOf("%") !=-1? true: false;
    }

    private String formatPercent(String percentage){
        BigDecimal bd = new BigDecimal(percentage.replace("%", "")).divide(BigDecimal.valueOf(100));
        return bd.toString();
    }

    /**
     * Creates a map w/ status and response to be returned to the client
     *
     * @param status, String - Informative message about the request status
     * @param success, boolean - Whether request succeeded or not
     * @return
     */
    private Map<String,String> mapResponse(String status, boolean success){
        Map<String, String> map = new HashMap<>();
        map.put("status", status);
        map.put("success", success ? "true" : "false");

        return map;
    }
}
