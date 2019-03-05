package org.mskcc.interop;

import org.mskcc.interop.model.Interop;
import org.mskcc.interop.repository.InteropRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.StringJoiner;

@RestController
@RequestMapping("/interop")
public class InteropController {

    @Autowired
    private InteropRepository interopRepository;
    private static Logger log = LoggerFactory.getLogger(InteropController.class);

    @GetMapping("/{runId}")
    public List<Interop> findByRunId(
            @PathVariable String runId,
            @RequestParam(name = "readNumber", required = false) String readNumberParam,
            @RequestParam(name = "lane", required = false) String laneParam
            ) {

        log.info("Run id: " + runId + ", read: " + readNumberParam + ", lane: " + laneParam);

        Integer readNumber = tryParseInteger(readNumberParam);
        Integer lane = tryParseInteger(laneParam);
        if (readNumber != null && lane != null) {
            return interopRepository.findByRunIdAndReadNumberAndLane(runId, readNumber, lane);
        } else if (readNumber != null) {
            return interopRepository.findByRunIdAndReadNumber(runId, readNumber);
        } else if (lane != null) {
            return interopRepository.findByRunIdAndLane(runId, lane);
        } else {
            return interopRepository.findByRunId(runId);
        }
    }

    private Integer tryParseInteger(String s) {
        Integer val;
        try {
            val = Integer.parseInt(s);
        } catch (Exception e) {
            val = null;
        }
        return val;
    }
}
