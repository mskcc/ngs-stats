package org.mskcc.sequencer;

import org.mskcc.sequencer.model.StartStopSequencer;
import org.mskcc.sequencer.repository.StartStopSequencerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
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
        String startedFileName = "RunInfo.xml";

        String runDirectoryName = baseDir + sequencer + "/" + run + "/";

        File startFile = new File(runDirectoryName + startedFileName);
        File endFile = new File(runDirectoryName + lastFile);
        if (!startFile.exists()) {
            log.error("File does not exist: " + startFile);
            return "File does not exist: " + startFile.getName();
        }

        log.info("Grabbing timestamps on file %s and file %s", startFile, endFile);
        Date startDate = new Date(startFile.lastModified());
        Date endDate = new Date(endFile.lastModified());

        StartStopSequencer startStop = new StartStopSequencer(run, sequencer, startDate, endDate, new Date());
        startStopSequencerRepository.save(startStop);
        return startStop.toString();
    }
}