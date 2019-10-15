package org.mskcc.cellranger.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ServletTests {
    @Autowired
    private MockMvc mockMvc;

    /**
     * Add all valid endpoints to the section of the request method allowed
     */
    final static String[] POST_REQUESTS = new String[]{
        "/saveCellRangerSample"
    };
    final static String[] GET_REQUESTS = new String[]{
        "/getCellRangerSample?project=cellranger&type=vdj",
        "/picardtoexcel",
        "/picardstats-controls/run/RUN_ID",
        "/picardstats/update/1",
        "/picardstats/updaterun/SEQUENCER/RUN",
        "/picardstats/run/RUN_ID",
        "/picardstats/run/RUN_ID/sample/SAMPLE_ID",
        "/picardstats/run-date/DATE_IN_MILLIS"
    };

    /**
     * Verifies get-endpoints ONLY allow get method requests
     * @throws Exception
     */
    @Test
    public void test_getRequests() throws Exception {

        for(String request : GET_REQUESTS){
            MvcResult result = this.mockMvc
                    .perform(get(request))
                    .andDo(print())
                    .andReturn();
            int status = result.getResponse().getStatus();
            assertFalse(status == 405);
        }
        for(String request : GET_REQUESTS){
            this.mockMvc.perform(post(request).content("{ \"k\": \"v\" }")).andDo(print()).andExpect(status().is(405));
            this.mockMvc.perform(put(request)).andDo(print()).andExpect(status().is(405));
            this.mockMvc.perform(delete(request)).andDo(print()).andExpect(status().is(405));
            this.mockMvc.perform(patch(request)).andDo(print()).andExpect(status().is(405));
        }
    }

    /**
     * Verifies post-endpoints ONLY allow post method requests
     * @throws Exception
     */
    @Test
    public void test_postRequests() throws Exception {
        for(String request : POST_REQUESTS){
            MvcResult result = this.mockMvc
                    .perform(post(request).contentType("application/json").content("{ \"k\": \"v\" }"))
                    .andDo(print())
                    .andReturn();
            int status = result.getResponse().getStatus();
            assertFalse(status == 405);
        }
        for(String request : POST_REQUESTS){
            this.mockMvc.perform(get(request)).andDo(print()).andExpect(status().is(405));
            this.mockMvc.perform(put(request)).andDo(print()).andExpect(status().is(405));
            this.mockMvc.perform(delete(request)).andDo(print()).andExpect(status().is(405));
            this.mockMvc.perform(patch(request)).andDo(print()).andExpect(status().is(405));
        }
    }
}
