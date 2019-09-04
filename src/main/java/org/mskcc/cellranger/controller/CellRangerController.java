package org.mskcc.cellranger.controller;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.mskcc.cellranger.model.CellRangerSummaryCount;
import org.mskcc.cellranger.model.CellRangerSummaryVdj;
import org.mskcc.cellranger.model.FieldSetter;
import org.mskcc.cellranger.repository.CellRangerSummaryCountRepository;
import org.mskcc.cellranger.repository.CellRangerSummaryVdjRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Entity;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
public class CellRangerController {
    // TODO - Put into constant file
    final String VDJ = "vdj";
    final String COUNT = "count";

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

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String testEndpoint(final HttpServletRequest request) {
        final String sample = request.getParameter("sample");
        final String type = request.getParameter("type");

        if( sample == null || type == null ) return String.format("ERROR: Bad Parameters. Sample: %s, Type: %s", sample, type);

        final String filePath = getWebSummaryPath(sample, type);
        final FieldMapperModel fieldMapperModel = getFieldMapperModel(type);
        final FieldSetter entity = getEntity(type);
        entity.setField("id", sample, String.class);

        try {
            populateEntity(filePath, fieldMapperModel, entity);
            createRowFromEntity(entity, type);
        } catch(IOException e){
            // TODO - logging
            return e.toString();
        }

        return "SUCCESS";
    }

    private void populateEntity(String fileName, FieldMapperModel fieldMapperModel, FieldSetter entity) throws IOException {
        // Requires full path
        File input = new File(fileName);
        Document doc = Jsoup.parse(input, "UTF-8", ""); // TODO - throws warning

        Element element;
        String value;
        String identifier;
        List<FieldMapper> fieldMapperList = fieldMapperModel.getFieldMapperList();
        for(FieldMapper fm : fieldMapperList){
            identifier = String.format("%s:contains(%s)",fm.getHtmlElement(),fm.getHtmlField());
            Elements matches = doc.select(identifier);
            // TODO - error checking for multiple matches
            for (Element label : matches) {
                element = label.nextElementSibling​();  // value follows field in html document
                value = element.text();
                entity.setField(fm.getTableField(), value, fm.getType());
            }
        }
    }

    private void createRowFromEntity(FieldSetter fs, String type){
        switch (type.toLowerCase()) {
            case "vdj":
                CellRangerSummaryVdj cellRangerSummaryVdj = (CellRangerSummaryVdj) fs;
                cellRangerSummaryVdjRepository.save(cellRangerSummaryVdj);
            case "count":
                CellRangerSummaryCount cellRangerSummaryCountModel = (CellRangerSummaryCount) fs;
                cellRangerSummaryCountRepository.save(cellRangerSummaryCountModel);
            default:
                break;
        }
    }

    private FieldSetter getEntity(String type){
        switch (type.toLowerCase()) {
            case "vdj":
                return new CellRangerSummaryCount();
            case "count":
                return new CellRangerSummaryCount();
            default:
                break;
        }
        return null;
    }

    private FieldMapperModel getFieldMapperModel(String type){
        switch (type.toLowerCase()) {
            case "vdj":
                return new CellRangerSummaryVDJModel();
            case "count":
                return new CellRangerSummaryCountModel();
            default:
                break;
        }
        return null;
    }

    private String getWebSummaryPath(String sample, String type){
        final String baseDir;
        switch (type.toLowerCase()) {
            case "vdj":  baseDir = CELL_RANGER_VDJ_DIR;
                break;
            case "count": baseDir = CELL_RANGER_COUNT_DIR;
                break;
            default:
                return String.format("ERROR: No corresponding web summary path for %s", type);
        }
        final String runPath = String.format("%s/%s%s", baseDir, sample, WEB_SUMMARY_PATH);
        return runPath;
    }
}
