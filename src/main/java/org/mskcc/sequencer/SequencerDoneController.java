package org.mskcc.sequencer;

import org.mskcc.sequencer.model.StartStopSequencer;
import org.mskcc.sequencer.repository.StartStopSequencerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FilenameFilter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("rundone")
public class SequencerDoneController {
    private static Logger log = LoggerFactory.getLogger(SequencerDoneController.class);

    @Autowired
    private StartStopSequencerRepository startStopSequencerRepository;

    @RequestMapping(value = "*", method = RequestMethod.GET)
    @ResponseBody
    public String getFallback() {
        return "Fallback for GET Requests";
    }

    @GetMapping(value = "/{sequencer}/{run}/{lastFile}")
    public String addRunTimes(@PathVariable String sequencer, @PathVariable String run, @PathVariable String lastFile) {
        String baseDir = "/ifs/input/GCL/hiseq/";
        String runDirectoryName = baseDir + sequencer + "/" + run + "/";

        Date startDate = findStartDateTime(runDirectoryName, sequencer);

        File endFile = new File(runDirectoryName + lastFile);
        Date endDate = null;
        if (endFile.exists()) { // run was started and is in progress or was stopped and will never complete
            endDate = new Date(endFile.lastModified());
        }

        log.info("Start and end time recorded for run: " + run);

        StartStopSequencer startStop = new StartStopSequencer(run, sequencer, startDate, endDate, new Date());
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