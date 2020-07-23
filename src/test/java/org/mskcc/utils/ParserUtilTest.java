package org.mskcc.utils;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ParserUtilTest {
    @Test
    public void parseCellRangerCsvLineTest_count1() {
        final String csvLine = "\"2,083\",\"73,429\",\"2,176\",\"152,952,980\",97.1%,81.5%,96.9%,94.5%,96.7%,96.6%,91.9%,7.1%,26.2%,58.6%,54.4%,2.5%,90.8%,\"23,332\",\"6,074\"";
        String[] expected = { "2083", "73429", "2176", "152952980", "0.971", "0.815", "0.969", "0.945", "0.967", "0.966", "0.919", "0.071", "0.262", "0.586", "0.544", "0.025", "0.908", "23332", "6074" };
        // TODO - add VDJ line
        List<String> parsedLine = ParserUtil.parseCellRangerCsvLine(csvLine);

        for(int i = 0; i<parsedLine.size(); i++){
            Assert.assertEquals(expected[i], parsedLine.get(i));
        }

        Assert.assertEquals(parsedLine.size(), expected.length);
    }

    @Test
    public void parseCellRangerCsvLineTest_count2() {
        final String csvLine = "\"5,130\",\"27,879\",757,\"143,023,961\",97.7%,54.2%,97.0%,94.8%,96.8%,97.3%,92.3%,5.4%,34.8%,52.0%,48.4%,2.4%,56.3%,\"23,009\",\"1,732\"";
        String[] expected = { "5130", "27879", "757", "143023961", "0.977", "0.542", "0.97", "0.948", "0.968", "0.973", "0.923", "0.054", "0.348", "0.52", "0.484", "0.024", "0.563", "23009", "1732" };
        List<String> parsedLine = ParserUtil.parseCellRangerCsvLine(csvLine);

        for(int i = 0; i<parsedLine.size(); i++){
            Assert.assertEquals(expected[i], parsedLine.get(i));
        }

        Assert.assertEquals(parsedLine.size(), expected.length);
    }

    @Test
    public void parseCellRangerCsvLineTest_vdj() {
        // TODO
    }

    @Test
    public void parseCellRangerCsvLineTest_edge() {
        final String csvLine = "95.5%";
        String[] expected = { "0.955" };

        List<String> parsedLine = ParserUtil.parseCellRangerCsvLine(csvLine);
        for(int i = 0; i<parsedLine.size(); i++){
            Assert.assertEquals(expected[i], parsedLine.get(i));
        }

        Assert.assertEquals(parsedLine.size(), expected.length);
    }
}
