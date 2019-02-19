import org.springframework.web.bind.annotation.*;

@RestController
public class PicardStatsController {

    @RequestMapping(value = "/picardstats/request/{requestid}", method = RequestMethod.GET)
    public String getQCSiteStats(@PathVariable String requestId) {
        return "needNewClass";
    }
}