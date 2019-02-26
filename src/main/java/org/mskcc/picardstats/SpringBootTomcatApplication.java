package org.mskcc.picardstats;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
/*
from: https://www.baeldung.com/spring-boot-war-tomcat-deploy
 */
public class SpringBootTomcatApplication extends SpringBootServletInitializer implements CommandLineRunner {
    @Autowired
    PicardStatsController c;

    public static void main(String[] args) {
        SpringApplication.run(SpringBootTomcatApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        if (args.length > 0 && args[0].equals("-buildDatabase"))
            c.buildDatabaseFromPicardFiles();
    }
}