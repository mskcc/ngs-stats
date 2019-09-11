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
    "******************************** DO NOT EDIT *********************************\n" +
    "******************************************************************************";

    private static final String FOUR_WHITESPACES = "    ";

    /**
     * Creates representation of java class. See javapoet
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
        builder.addField(idSpec);

        for(FieldMapper fm : fieldMappers)
            builder.addField(getFieldSpec(fm));

        return builder.build();
    }

    /**
     * Generates model field based on mapping described in documentation
     */
    private static FieldSpec getFieldSpec(FieldMapper fieldMapper){
        final Class type = fieldMapper.getType();

        com.squareup.javapoet.FieldSpec.Builder fieldBuilder = FieldSpec
                .builder(fieldMapper.getType(), fieldMapper.getTableField())
                .addModifiers(Modifier.PUBLIC);

        // No string field should exceed 64 characters
        if(type == String.class){
            fieldBuilder.addAnnotation(AnnotationSpec.builder(Column.class)
                    .addMember("length", "64")
                    .build());
        }

        return fieldBuilder.build();
    }

    public static void generateModelFile(String javaRoot, String pkgName, String className, FieldMapperModel fieldMapperModel) throws IOException {
        TypeSpec typeSpec = createTypeSpec(className, fieldMapperModel.getFieldMapperList());
        writeToOutputFile(javaRoot, pkgName, typeSpec);
        log.info(String.format("Created model class %s in package %s", className, pkgName));
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