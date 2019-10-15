package org.mskcc;

import org.mskcc.cellranger.controller.CellRangerController;
import org.mskcc.picardstats.PicardStatsController;
import org.mskcc.sequencer.SequencerDoneController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


/*
from: https://www.baeldung.com/spring-boot-war-tomcat-deploy w/Swagger2 added
 */
@SpringBootApplication
@EnableSwagger2
public class SpringBootTomcatApplication extends SpringBootServletInitializer implements CommandLineRunner {
    @Autowired
    private PicardStatsController c;

    @Autowired
    private CellRangerController cellRangerController;

    @Autowired
    private SequencerDoneController x;

    public static void main(String[] args) {
        SpringApplication.run(SpringBootTomcatApplication.class, args);
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(true)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("NGS Stats")
                .description("Picard Stats, Sequencer Start/Stop Times, Archived Fastq.gz File Paths.")
                .contact(new Contact("The IGO Data Team", "https://igo.mskcc.org", "zzPDL_SKI_IGO_DATA@mskcc.org"))
                .build();
    }

    @Override
    public void run(String... args) throws Exception {
        if (args.length > 0 && args[0].equals("-buildDatabase"))
            c.buildDatabaseFromPicardFiles();
    }
}