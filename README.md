A Java/SpringBoot, Tomcat & MySQL application initiated in 2018 originally running on delphi.mskcc.org (now igodb.mskcc.org) which has 17 tables with IGO (Integrated Genomics Operation) post sequencing information such as:

File name information for over 3.4 million fastq files written by IGO since 2011 in the table archivedfastq as of June 2025.

Sequencer start-stop times in the table startstopsequencer so the lab can track sequencer availability.

10x Cell Ranger Stats in tables cellrangersummaryvdj and cellrangersummarycount.

Code to parse several Broad picard.jar output files and store in the database - HS Metrics, Duplication Metrics, Alignment Summary Metrics, RNA Seq Metrics & WGS Metrics, and crosscheckmetrics and code to convert Illumina DRAGEN stats into the GATK/Picard format and load into the ngs-stats database.

Picard naming conventions follow the Picard Metrics Definitions:

https://broadinstitute.github.io/picard/picard-metric-definitions.html

This project uses Lombok, to build with IntelliJ or Eclipse see:
https://www.baeldung.com/lombok-ide
