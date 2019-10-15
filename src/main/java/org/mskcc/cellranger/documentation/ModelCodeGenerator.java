package org.mskcc.cellranger.documentation;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;
import lombok.NoArgsConstructor;
import org.mskcc.cellranger.model.CellRangerDataRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.lang.model.element.Modifier;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class ModelCodeGenerator {
    private static final String FOUR_WHITESPACES = "    ";
    private static Logger log = LoggerFactory.getLogger(ModelCodeGenerator.class);
    private static String WARNING =
            "─────────────────▄████▄\n" +
                    "─────▄▄▄▄▄▄▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▄▄▄▄▄▄▄\n" +
                    "───▄▀░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░▀▄\n" +
                    "──▐░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░▌\n" +
                    "──▐░░██████░░███████░░█████░░░██████░░▌\n" +
                    "──▐░░██░░░░░░░░██░░░░██░░░██░░██░░░██░▌\n" +
                    "──▐░░██████░░░░██░░░░██░░░██░░██████░░▌\n" +
                    "──▐░░░░░░██░░░░██░░░░██░░░██░░██░░░░░░▌\n" +
                    "──▐░░██████░░░░██░░░░░████░░░░██░░░░░░▌\n" +
                    "──▐░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░▌\n" +
                    "───▀▄░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░▄▀\n" +
                    "─────▀▀▀▀▀▀▀▀▀▀▀▀██████▀▀▀▀▀▀▀▀▀▀▀▀▀\n" +
                    "──────────────────█▀▀█\n" +
                    "──────────────────█▀▀█\n" +
                    "──────────────────█▀▀█\n" +
                    "──────────────────█▀▀█\n" +
                    "──────────────────█▀▀█\n" +
                    "──────────────────█▀▀█\n" +
                    "──────────────────█▀▀█\n" +
                    "──────────────────█▀▀█\n" +
                    "──────────────────█▀▀█\n" +
                    "──────────────────█▀▀█\n" +
                    "──────────────────█▀▀█\n" +
                    "──────────────────█▀▀█\n" +
                    "──────────────────█▀▀█\n" +
                    "──────────────────█▀▀█\n" +
                    "──────────────────█▀▀█\n" +
                    "──────────────────█▀▀█\n" +
                    "──────────────────█▀▀█\n" +
                    "─────────────────█▀▀▀▀█\n" +
                    "─────────────────█▀▀▀▀█\n" +
                    "─────────────────█▀▀▀▀█\n" +
                    "─────────────────█▀▀▀▀█\n" +
                    "─────────────────▀████▀ \n" +
                    "\n" +
                    "******************************************************************************\n" +
                    "***************************** DO NOT EDIT/DELETE *****************************\n" +
                    "******************************************************************************";

    /**
     * Generates Model file, org.mskcc.cellranger.model.[CLASS_NAME]
     *
     * @param javaRoot, String - path to java directory, e.g. "./src/main/java"
     * @param pkgName, String, - package name for models, e.g. "org.mskcc.cellranger.model"
     * @param className, String - Name of class to be created, e.g. "CellRangerSummaryCount"
     * @param fieldMapperModel, FieldMapperModel - Implemented Model file, e.g. CellRangerSummaryCountModel
     * @throws IOException
     */
    public static void generateModelFile(String javaRoot, String pkgName, String className, FieldMapperModel fieldMapperModel) throws IOException {
        TypeSpec typeSpec = createTypeSpec(className, fieldMapperModel.getFieldMapperList());
        writeToOutputFile(javaRoot, pkgName, typeSpec);
        log.info(String.format("Created model class %s in package %s", className, pkgName));
    }

    /**
     * Creates representation of java class using javapoet
     *
     * @param className, String - Name of class, e.g. "CellRangerSummaryCount"
     * @param fieldMappers, List - List of fields that should be included in model
     * @return
     */
    private static TypeSpec createTypeSpec(String className, List<FieldMapper> fieldMappers) {
        Builder builder = TypeSpec
                .classBuilder(className)
                .superclass(CellRangerDataRecord.class)
                .addJavadoc(WARNING)
                .addAnnotation(Entity.class)
                .addAnnotation(NoArgsConstructor.class)
                .addModifiers(Modifier.PUBLIC);

        FieldSpec idSpec = FieldSpec
                .builder(String.class, "id")
                .addAnnotation(Id.class)
                .addAnnotation(AnnotationSpec.builder(Column.class)
                        .addMember("length", "128")
                        .build())
                .addModifiers(Modifier.PUBLIC)
                .build();
        FieldSpec runSpec = FieldSpec
                .builder(String.class, "run")
                .addAnnotation(AnnotationSpec.builder(Column.class)
                        .addMember("length", "128")
                        .build())
                .addModifiers(Modifier.PUBLIC)
                .build();
        FieldSpec projectSpec = FieldSpec
                .builder(String.class, "project")
                .addAnnotation(AnnotationSpec.builder(Column.class)
                        .addMember("length", "128")
                        .build())
                .addModifiers(Modifier.PUBLIC)
                .build();

        builder.addField(idSpec);
        builder.addField(runSpec);
        builder.addField(projectSpec);

        for (FieldMapper fm : fieldMappers)
            builder.addField(getFieldSpec(fm));

        return builder.build();
    }

    /**
     * Generates model field based on mapping described in documentation
     *
     * @param fieldMapper, FieldMapper - E.g. {
     *      "htmlElement": "h1",
     *      "htmlField": "Estimated Number of Cells",
     *      "tableField": "EstimatedNumberOfCells"
     * }
     * @return, FieldSpec - javapoet class that can create field in java class
     */
    private static FieldSpec getFieldSpec(FieldMapper fieldMapper) {
        final Class type = fieldMapper.getType();

        com.squareup.javapoet.FieldSpec.Builder fieldBuilder = FieldSpec
                .builder(fieldMapper.getType(), fieldMapper.getTableField())
                .addModifiers(Modifier.PUBLIC);

        // No string field should exceed 64 characters
        if (type == String.class) {
            // TODO - If more exceptional cases are needed, move this to a seperate field in the FieldMapper
            if (fieldMapper.getTableField().equals("CompressedGraphData")) {
                // Should generate text data type (Max-Length: 16,777,215 - 16 MB)
                fieldBuilder.addAnnotation(AnnotationSpec.builder(Column.class)
                        .addMember("length", "2097152")
                        .addMember("columnDefinition", "\"TEXT\"")
                        .build());
            } else {
                fieldBuilder.addAnnotation(AnnotationSpec.builder(Column.class)
                        .addMember("length", "64")
                        .build());
            }
        }

        return fieldBuilder.build();
    }

    private static void writeToOutputFile(String javaRoot, String packageName, TypeSpec typeSpec) throws IOException {
        final File outputFile = new File(Paths.get(new File(javaRoot).getAbsolutePath()).toUri());
        JavaFile javaFile = JavaFile
                .builder(packageName, typeSpec)
                .indent(FOUR_WHITESPACES)
                .build();
        javaFile.writeTo(outputFile);
    }
}