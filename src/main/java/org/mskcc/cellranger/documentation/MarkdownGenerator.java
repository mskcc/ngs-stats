package org.mskcc.cellranger.documentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class MarkdownGenerator {
    private static Logger log = LoggerFactory.getLogger(MarkdownGenerator.class);

    /**
     * Removes current schema file
     *
     * @param schemaFile
     * @return
     */
    public static boolean removeMarkdownFile(String schemaFile) {
        File file = new File(schemaFile);
        if(file.delete())
        {
            log.info(String.format("Deleted %s", schemaFile));
            return true;
        }
        log.info(String.format("Failed to delete %s", schemaFile));
        return false;
    }

    /**
     * Generates the markdown file w/ table schema information for the model.
     * Describes how controller parses DOM elements of cell ranger's output file, web_summary.html, and maps to model
     *
     * @param modelName, String     - classname of model
     * @param fieldMapperModel, Map - Map containing schema information
     * @return
     */
    private static String generateMarkDownTable(String modelName, FieldMapperModel fieldMapperModel){
        List<FieldMapper> fieldMapperList = fieldMapperModel.getFieldMapperList();
        String markdownTable = String.format(   "\n# %s\n\n" +
                "| Html Element | Html Label | Table Field | Table Type |\n" +
                "| ------------ | ---------- | ----------- | ---------- |\n", modelName);
        for(FieldMapper fm : fieldMapperList){
            markdownTable = String.format("%s| %s | %s | %s | %s |\n", markdownTable, fm.getHtmlElement(), fm.getHtmlField(), fm.getTableField(), fm.getType());
        }
        return markdownTable;
    }

    /**
     * Writes markdown to input markdownFileName
     *
     * @param markdownFileName
     * @param modelName
     * @param fieldMapperModel
     */
    public static void writeMarkdown(String markdownFileName, String modelName, FieldMapperModel fieldMapperModel){
        String markDownTable = generateMarkDownTable(modelName, fieldMapperModel);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(markdownFileName, true));
            writer.write(markDownTable);
            writer.close();
            log.info(String.format("Added markdown for %s to %s", modelName, markdownFileName));
        } catch(IOException e) {
            log.error(String.format("Error adding markdown for %s to %s", modelName, markdownFileName));
        }
    }
}
