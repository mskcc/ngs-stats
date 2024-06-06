package org.mskcc.sequencer;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.TrustStrategy;
import org.mskcc.sequencer.model.ArchivedFastq;
import org.mskcc.sequencer.model.StartStopArchiveFastq;
import org.mskcc.sequencer.model.StartStopSequencer;
import org.mskcc.sequencer.model.dto.SampleQcSummary;
import org.mskcc.sequencer.repository.ArchivedFastqRepository;
import org.mskcc.sequencer.repository.StartStopArchiveFastqRepository;
import org.mskcc.sequencer.repository.StartStopSequencerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.InterceptingClientHttpRequestFactory;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("rundone")
public class SequencerDoneController {
    private static Logger log = LoggerFactory.getLogger(SequencerDoneController.class);

    @Autowired
    private StartStopSequencerRepository startStopSequencerRepository;

    @Autowired
    private StartStopArchiveFastqRepository startStopArchiveFastqRepository;

    @Autowired
    private ArchivedFastqRepository archivedFastqRepository;

    @Value("${lims.rest.url}")
    private String limsUrl;
    @Value("${lims.rest.username}")
    private String limsUser;
    @Value("${lims.rest.password}")
    private String limsPass;

    @GetMapping(value = "/getRecentlyArchivedRequests/{minutes}")
    public List<String> getRecentlyArchivedRequests(@PathVariable Integer minutes) {
        if (minutes < 1) {
            String errorDetails = "Minutes argument must be 1 or greater.";
            new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
        }
        // convert minutes to start date
        Date startDate = new Date(System.currentTimeMillis() - (minutes * 60000));
        String timestampString = new SimpleDateFormat("dd-M-yyyy hh:mm:ss").format(startDate);
        log.info(String.format("Finding requests archived in the last %s minutes, after %s", minutes, timestampString));
        return archivedFastqRepository.findRecentlyArchived(startDate);
    }

    @GetMapping(value = "/getpoolednormals/{request}")
    public List<ArchivedFastq> getPooledNormals(@PathVariable String request) {
        log.info("Finding pooled normals for request:" + request);
        if (request.length() < 5 || request.length() > 10)
            return null;

        return archivedFastqRepository.findAllPooledNormals(request);
    }

    /**
     * For external IGO Customers to determine when a fastq is ready and they can start their pipeline.
     * @param sample
     * @return
     */
    @GetMapping(value = "/sampleigocomplete/{sample}")
    public List<ArchivedFastq> findDeliveredFastq(@PathVariable String sample) {
        log.info("Finding delivered fastqs for sample:" + sample);
        if (sample.contains(" ")) // invalid sample id
            return null;

        List<ArchivedFastq> fastqs = archivedFastqRepository.findBySampleStartsWith(sample + "_IGO_");
        if (fastqs != null) {
            if (sampleIsIGOComplete(sample, fastqs, limsUrl, limsUser, limsPass))
                return fastqs;
        }
        return null;
    }

    protected boolean sampleIsIGOComplete(String sample, List<ArchivedFastq> fastqs, String limsUrl, String limsUser, String limsPass) {
        String url = limsUrl + "/getIGOCompleteQC?sampleId=" + sample;
        log.info("Checking if sample is IGO complete via: " + url);

        RestTemplate rt = limsRestTemplate();
        ResponseEntity<List<SampleQcSummary>> response = rt.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<SampleQcSummary>>(){});
        List<SampleQcSummary> qcSummaries = response.getBody();
        if (qcSummaries != null && qcSummaries.size() > 0) {
            log.info("Samples that are igo complete: " + qcSummaries.size());
            if (sample.equals(qcSummaries.get(0).getSampleName()))
                return true;
        }
        log.info("No IGO complete records found for sample: " + sample);
        return false;
    }

    @GetMapping(value = "/fastqsbyigoid/{igoid}")
    public List<ArchivedFastq> findFastqsByIGOId(@PathVariable String igoid) {
        log.info("/fastqsbyigoid/" + igoid);
        if (igoid == null || igoid.length() < 7)
            return null;

        List<ArchivedFastq> fastqs = archivedFastqRepository.findBySampleEndsWith(igoid);
        return fastqs;
    }

    @GetMapping(value = "/fastqsbyproject/{project}")
    public List<ArchivedFastq> findFastqsByProject(@PathVariable String project) {
        log.info("/findFastqsByProject/" + project);
        // Project 4786 -> must allow old format non zero prepended projects
        if (project == null || project.length() < 4 || project.length() > 9)
            return null;

        List<ArchivedFastq> fastqs = archivedFastqRepository.findByProject(project);
        return fastqs;
    }

    @GetMapping(value = "/search/most/recent/fastqpath/{run}/{sampleAndigoid}")
    public List<ArchivedFastq> findMostRecentFastqDir(@PathVariable String run, @PathVariable String sampleAndigoid) {
        log.info("/search/most/recent/fastqpath/ for run:" + run + " sampleAndigoid:" + sampleAndigoid);
        List<ArchivedFastq> fastqs =
                archivedFastqRepository.findByRunStartsWithAndSampleOrderByFastqLastModifiedDesc(run, sampleAndigoid);

        if (fastqs == null || fastqs.size() == 0) {
            // TODO test for fastq.gz files written prior to 2016-1-20
            // Try query again with project removing leading '0' because from 2016-1-20 & prior fastq.gz files had the leading zero removed
//            if (project.startsWith("0") && sampleAndigoid.contains("_IGO_")) {
//                return findMostRecentFastqDir(project.substring(1), sample.substring(0, sample.indexOf("_IGO_")), run);
//            }
            log.error("null found");
            return null;
        } else {
            // TODO filter old runs when there is a manual redemux like MICHELLE_0098_BHJCFJDMXX_A1
            log.info("Found fastq.gz: " + fastqs.size());
            return fastqs;
        }
    }

    @GetMapping(value = "/fastq/{directoryName}")
    public String trackDemuxAndArchiving(@PathVariable String directoryName) {
        String baseDir = "/igo/delivery/FASTQ/";
        String fastqDirName = baseDir + directoryName;
        File fastqDir = new File(fastqDirName);

        if (!fastqDir.exists()) {
            return "Directory does not exist - " + fastqDirName;
        }

        File statsDir = new File(fastqDir + "/Stats");
        File finished = new File(fastqDir + "/FINISHED");
        File archiveDone = new File(fastqDir + "/fastq.md5.archive");
        Date demuxDone = null;
        if (statsDir.exists())
            demuxDone = new Date(statsDir.lastModified());
        Date finishedDate = new Date(finished.lastModified());
        Date archivedDate = new Date(archiveDone.lastModified());

        // extract sequencer name & run name from directory Name
        String sequencer = StartStopArchiveFastq.getSequencerName(directoryName);
        String run = StartStopArchiveFastq.getRun(directoryName);

        StartStopArchiveFastq startStop =
                new StartStopArchiveFastq(directoryName, run, sequencer, demuxDone, finishedDate, archivedDate, new Date());
        log.info("Saving database archiving times: " + startStop);
        startStopArchiveFastqRepository.save(startStop);

        try {
            List<ArchivedFastq> fastqs = ArchivedFastq.walkDirectory(baseDir, directoryName);
            archivedFastqRepository.saveAll(fastqs);
            log.info(fastqs.size() + " fastq.gz file paths saved to the database.");
        } catch (IOException e) {
            log.error("Failed to save fastq.gz files to database.", e);
        }
        return startStop.toString();
    }

    @GetMapping(value = {"/sequencerstartstop/{sequencer}/{run}/{lastFile}"})
    public String addRunTimes(@PathVariable String sequencer, @PathVariable String run, @PathVariable String lastFile) {
        String baseDir = "/igo/sequencers/";

        if ("pepe".equals(sequencer)) // PEPE is currently IGO's only NextSeq 2000 which has a new directory structure
            sequencer += "/output";

        String runDirectoryName = baseDir + sequencer + "/" + run + "/";
        if (!new File(runDirectoryName).exists()) {
            log.error("Run directory does not exist: " + runDirectoryName);
            return null;
        }
        log.info("Grabbing sequencer start/stop times for directory: " + runDirectoryName);

        Date startDate = findStartDateTime(runDirectoryName, sequencer);

        File endFile = new File(runDirectoryName + lastFile);
        Date endDate = null;
        if (endFile.exists()) { // run was started and is in progress or was stopped and will never complete
            endDate = new Date(endFile.lastModified());
        }

        String runMinusDate = StartStopSequencer.removeDateFromRun(run);
        StartStopSequencer startStop = new StartStopSequencer(run, runMinusDate, sequencer, startDate, endDate, new Date());
        log.info("Saving to database sequencer start/stop times: " + startStop);
        startStopSequencerRepository.save(startStop);
        return startStop.toString();
    }

    protected static Date findStartDateTime(String runDirectoryName, String sequencer) {
        log.info("RunDirectory:%s ,sequencer:%s", runDirectoryName, sequencer);
        // Normally we'll try to rely on the timestamp on RunInfo.xml, for HiSeq4000 & NextSeq that does not work

        if ("jax".equals(sequencer) || "pitt".equals(sequencer)) { // HiSeq4000
            log.info("Using timestamp in name of first log file.");
            // Parse Date in the filename named like "Logs/h2hcybbxy_2018-10-01T14-09-50 Read #1 Cycle #1.log"
            File logDirectory = new File(runDirectoryName + "/Logs");
            String [] files = logDirectory.list(new SuffixFilenameFilter("Read #1 Cycle #1.log"));
            if (files == null)
                return null;
            else {
                String dateString = files[0].substring(10, 29);
                String pattern = "yyyy-MM-dd'T'HH-mm-ss"; // 2018-10-01T14-09-50
                SimpleDateFormat simpleDateFormat= new SimpleDateFormat(pattern);
                try {
                    Date date = simpleDateFormat.parse(dateString);
                    return date;
                } catch (ParseException e) {
                    log.error(e.getMessage());
                }
            }
        } else if ("scott".equals(sequencer)) { // NextSeq
            File logDirectoryScott = new File(runDirectoryName + "/Logs");
            String [] filesScott = logDirectoryScott.list(new SuffixFilenameFilter("_Cycle1_Log.00.xml"));
            if (filesScott == null) {
                log.error(("No scott log files found in : " + logDirectoryScott.getAbsolutePath()));
                return null;
            } else {
                File firstLogFileScott = new File(logDirectoryScott + "/" + filesScott[0]);
                if (firstLogFileScott.exists()) {
                    log.info("First log file: " + firstLogFileScott.getName() + " " + firstLogFileScott.lastModified() + " path: " + firstLogFileScott.getAbsolutePath());
                    return new Date(firstLogFileScott.lastModified());
                } else {
                    log.error("File does not exist:" + firstLogFileScott);
                    return null;
                }
            }
        }

        String startedFileName = "RunInfo.xml";
        File startFile = new File(runDirectoryName + startedFileName);
        if (!startFile.exists()) {
            log.error("File does not exist: " + startFile);
            return null;
        }

        Date startDate = new Date(startFile.lastModified());
        return startDate;
    }

    public RestTemplate limsRestTemplate() {
        RestTemplate restTemplate = getInsecureRestTemplate();
        addBasicAuth(restTemplate, limsUser, limsPass);
        return restTemplate;
    }
    private static void addBasicAuth(RestTemplate restTemplate, String username, String password) {
        List<ClientHttpRequestInterceptor> interceptors = Collections.singletonList(new BasicAuthorizationInterceptor(username, password));
        restTemplate.setRequestFactory(new InterceptingClientHttpRequestFactory(restTemplate.getRequestFactory(),
                interceptors));
    }
    public static RestTemplate getInsecureRestTemplate() {
        try {
            TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
            SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
                    .loadTrustMaterial(null, acceptingTrustStrategy)
                    .build();
            SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);
            CloseableHttpClient httpClient = HttpClients.custom()
                    .setSSLSocketFactory(csf)
                    .build();
            HttpComponentsClientHttpRequestFactory requestFactory =
                    new HttpComponentsClientHttpRequestFactory();
            requestFactory.setHttpClient(httpClient);

            return new RestTemplate(requestFactory);
        } catch (Exception e) {
            throw new RuntimeException("Error while initializing insecure rest template", e);
        }
    }

    public static class SuffixFilenameFilter implements FilenameFilter {
        private String suffix;
        public SuffixFilenameFilter(String suffix) {
            this.suffix = suffix;
        }
        @Override
        public boolean accept(File dir, String name) {
            if (name.endsWith(suffix))
                return true;
            return false;
        }
    }
}