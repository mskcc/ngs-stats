package org.mskcc.cellranger.documentation;

import org.mskcc.cellranger.model.CellRangerDataRecord;
import org.mskcc.picardstats.PicardStatsController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Generates the org.mskcc.cellranger.model files w/ accompanying documentation in org.mskcc.cellranger.documentation.
 * Should run as a seperate gradle task configured in build.grade > "generateDocs"
 * Steps:
 *      1) Refresh Gradle
 *      2) ngs-stats > Tasks > other > buildModels
 */
public class CodeGenerator {
    private final static String ROOT_PATH = "./src/main/java";
    private static Logger log = LoggerFactory.getLogger(PicardStatsController.class);
    private static Map<String, FieldMapperModel> classToFieldModelMap = new HashMap<String, FieldMapperModel>();
    private static String SCHEMA_FILE_PATH;
    private static String MODEL_PKG_NAME;

    static {
        /*
            ADD MODELS HERE
         */
        // classToFieldModelMap.put(FILE_NAME, new DOCUMENTATION_MODEL());
        classToFieldModelMap.put("CellRangerSummaryCount", new CellRangerSummaryCountModel());
        classToFieldModelMap.put("CellRangerSummaryVdj", new CellRangerSummaryVDJModel());

        SCHEMA_FILE_PATH = String.format("%s/README.md", getDocumentationPath());
        MODEL_PKG_NAME = getModelPkgName();
    }

    private static String getDocumentationPath() {
        CodeGenerator codeGenerator = new CodeGenerator();
        return getPathToPackage(codeGenerator);
    }

    private static String getModelPkgName() {
        CellRangerDataRecord model = new CellRangerDataRecord();
        return getPkgName(model);
    }

    private static String getPkgName(Object obj) {
        return obj.getClass().getPackage().getName();
    }

    private static String getPathToPackage(Object obj) {
        final String pkgName = getPkgName(obj);
        final String pkgPath = pkgName.replace(".", "/");

        return String.format("%s/%s", ROOT_PATH, pkgPath);
    }

    public static void main(String[] args) {
        MarkdownGenerator.removeMarkdownFile(SCHEMA_FILE_PATH);
        for (Map.Entry<String, FieldMapperModel> entry : classToFieldModelMap.entrySet()) {
            try {
                String dataRecordName = entry.getKey();
                ModelCodeGenerator.generateModelFile(ROOT_PATH, MODEL_PKG_NAME, dataRecordName, entry.getValue());
                MarkdownGenerator.writeMarkdown(SCHEMA_FILE_PATH, entry.getKey(), entry.getValue());
            } catch (IOException e) {
                log.error(String.format("Failed to generate model and documentation for %s. Error: %s",
                        entry.getKey(), e.getMessage()));
            }
        }
    }
}