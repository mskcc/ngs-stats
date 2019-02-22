package org.mskcc.picardstats;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StatsApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(StatsApplication.class, args);
    }

    @Override
    public void run(String... args) {
    }
}