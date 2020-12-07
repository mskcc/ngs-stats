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

        List<String> parsedLine = ParserUtil.parseCellRangerCsvLine(csvLine);
        String expectedVal, parsedVal;
        for(int i = 0; i<parsedLine.size(); i++){
            expectedVal = expected[i];
            parsedVal = parsedLine.get(i);
            Assert.assertEquals(String.format("%s != %s", expectedVal, parsedVal), expectedVal, parsedVal);
        }

        Assert.assertEquals(parsedLine.size(), expected.length);
    }

    @Test
    public void parseCellRangerCsvLineTest_count2() {
        final String csvLine = "\"5,130\",\"27,879\",757,\"143,023,961\",97.7%,54.2%,97.0%,94.8%,96.8%,97.3%,92.3%,5.4%,34.8%,52.0%,48.4%,2.4%,56.3%,\"23,009\",\"1,732\"";
        String[] expected = { "5130", "27879", "757", "143023961", "0.977", "0.542", "0.97", "0.948", "0.968", "0.973", "0.923", "0.054", "0.348", "0.52", "0.484", "0.024", "0.563", "23009", "1732" };

        List<String> parsedLine = ParserUtil.parseCellRangerCsvLine(csvLine);
        String expectedVal, parsedVal;
        for(int i = 0; i<parsedLine.size(); i++){
            expectedVal = expected[i];
            parsedVal = parsedLine.get(i);
            Assert.assertEquals(String.format("%s != %s", expectedVal, parsedVal), expectedVal, parsedVal);
        }

        Assert.assertEquals(parsedLine.size(), expected.length);
    }

    @Test
    public void parseCellRangerCsvLineTest_vdj() {
        final String csvLine = "\"6,174\",\"10,455\",\"5,499\",\"64,550,286\",96.8%,97.4%,96.0%,95.0%,97.0%,91.2%,40.5%,50.5%,\"7,926\",82.0%,11.1,16.0,89.1%,89.1%,53.55,98.1%,99.1%,97.1%,98.8%,96.9%,98.4%,93.4%,95.7%";
        String[] expected = { "6174", "10455", "5499", "64550286", "0.968", "0.974", "0.96", "0.95", "0.97", "0.912", "0.405", "0.505", "7926", "0.82", "11.1", "16.0", "0.891", "0.891", "53.55", "0.981", "0.991", "0.971", "0.988", "0.969", "0.984", "0.934", "0.957" };

        List<String> parsedLine = ParserUtil.parseCellRangerCsvLine(csvLine);
        String expectedVal, parsedVal;
        for(int i = 0; i<parsedLine.size(); i++){
            expectedVal = expected[i];
            parsedVal = parsedLine.get(i);
            Assert.assertEquals(String.format("%s != %s", expectedVal, parsedVal), expectedVal, parsedVal);
        }

        Assert.assertEquals(expected.length, parsedLine.size());
    }

    @Test
    public void parseCellRangerCsvLineTest_edge() {
        final String csvLine = "95.5%";
        String[] expected = { "0.955" };

        List<String> parsedLine = ParserUtil.parseCellRangerCsvLine(csvLine);
        String expectedVal, parsedVal;
        for(int i = 0; i<parsedLine.size(); i++){
            expectedVal = expected[i];
            parsedVal = parsedLine.get(i);
            Assert.assertEquals(String.format("%s != %s", expectedVal, parsedVal), expectedVal, parsedVal);
        }

        Assert.assertEquals(parsedLine.size(), expected.length);
    }

    @Test
    public void parseCellRangerCsvLineTest_edge2() {
        final String csvLine = "\"2,166\",\"49,188\",218,\"106,542,626\",81.5%,95.0%,95.8%,93.7%,88.4%,95.3%,75.9%,60.8%,15.5%,3.8%,30.5%,28.9%,7.2%,54.0%,\"17,651\",369";
        String[] expected = { "2166", "49188", "218", "106542626", "0.815", "0.95", "0.958", "0.937", "0.884", "0.953", "0.759", "0.608", "0.155", "0.038", "0.305", "0.289", "0.072", "0.54", "17651", "369" };

        List<String> parsedLine = ParserUtil.parseCellRangerCsvLine(csvLine);
        String expectedVal, parsedVal;
        for(int i = 0; i<parsedLine.size(); i++){
            expectedVal = expected[i];
            parsedVal = parsedLine.get(i);
            Assert.assertEquals(String.format("%s != %s", expectedVal, parsedVal), expectedVal, parsedVal);
        }

        Assert.assertEquals(parsedLine.size(), expected.length);
    }
}
