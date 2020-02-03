package org.mskcc.crosscheckmetrics.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mskcc.crosscheckmetrics.CrossCheckMetricsController;
import org.mskcc.crosscheckmetrics.model.CrosscheckMetrics;
import org.mskcc.crosscheckmetrics.model.CrosscheckMetricsId;
import org.mskcc.crosscheckmetrics.model.ProjectEntries;
import org.mskcc.crosscheckmetrics.respository.CrossCheckMetricsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.DelegatingServletInputStream;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CrosscheckMetricsControllerTest {
    private static Logger log = LoggerFactory.getLogger(CrosscheckMetricsControllerTest.class);
    @Captor
    ArgumentCaptor<CrosscheckMetrics> crosscheckMetricsArgumentCaptor;
    @Mock
    HttpServletRequest request;
    @InjectMocks
    private CrossCheckMetricsController crossCheckMetricsController;
    @Mock
    private CrossCheckMetricsRepository crossCheckMetricsRepository;

    /**
     * The production filesystem will have a directory that stores crosscheck metrics in the following structure,
     * CROSSCHECK_METRICS_DIR
     * |- RUN
     * |   |-PROJECT
     * |   \-(...)
     * (...)
     * <p>
     * We point the test to a local test directory with the same file setup and a mock file
     *
     * @throws IOException
     */
    @Before
    public void createMockCellRangerDir() throws IOException {
        MockitoAnnotations.initMocks(this);

        final String projectPath = FileSystems.getDefault().getPath(".").toAbsolutePath().toString();
        final String pkgName = new CrosscheckMetricsControllerTest().getClass().getPackage().getName();
        final String pkgPath = pkgName.replace(".", "/");
        final String path = String.format("%s/src/test/java/%s/mocks", projectPath, pkgPath);

        ReflectionTestUtils.setField(crossCheckMetricsController, "CROSSCHECK_METRICS_DIR", path);
    }

    /**
     * Creates request body as it would be sent in request. Mocks it as the input stream of the HttpServletRequest
     *
     * @param values, map of "field" -> "value" entries that should be put into the mocked request
     * @throws IOException
     */
    private void setupRequest(Map<String, String> values) throws IOException {
        String fields = "";
        for (Map.Entry<String, String> entry : values.entrySet()) {
            fields = String.format("\"%s\": \"%s\"", entry.getKey(), entry.getValue());
        }
        String requestBody = String.format("{%s}", String.join(",", fields));
        when(request.getInputStream()).thenReturn(
                new DelegatingServletInputStream(
                        new ByteArrayInputStream(requestBody.getBytes(StandardCharsets.UTF_8))));
    }

    @Test
    public void readCrosscheckMetrics_success() {
        final String TEST_PROJECT = "PROJECT";
        final String TEST_RESULT = "EXPECTED_MATCH";
        Map<String, String> request = new HashMap<>();
        request.put("project", TEST_PROJECT);
        try {
            setupRequest(request);
        } catch (IOException e) {
            log.error(String.format("Error with test setup. %s", e.getMessage()));
            return;
        }

        // Setup database get calls
        List<CrosscheckMetrics> mockResponse = new ArrayList<>();
        CrosscheckMetrics mockCrosscheckMetrics = new CrosscheckMetrics();
        mockCrosscheckMetrics.crosscheckMetricsId = new CrosscheckMetricsId(TEST_PROJECT, "dummy1", "dummy2");
        mockCrosscheckMetrics.result = TEST_RESULT;
        mockResponse.add(mockCrosscheckMetrics);
        when(crossCheckMetricsRepository.findByCrosscheckMetricsId_Project(new ArrayList<>(Arrays.asList(TEST_PROJECT)))).thenReturn(mockResponse);
        Map<String, Object> response = crossCheckMetricsController.getCrosscheckMetrics(TEST_PROJECT);

        // Verify a successful response
        assertEquals("true", response.get("success"));

        Map<String, ProjectEntries> data = (Map<String, ProjectEntries >)response.get("data");
        ProjectEntries pe = data.get(TEST_PROJECT);
        Set<String> results = pe.getResults();

        assertEquals(1, pe.getEntries().size());
        assertTrue(results.contains(TEST_RESULT));
        assertTrue(results.size() == 1);
    }

    @Test
    public void writeCrosscheckMetrics_success() {
        /**
         * Names should come from the hierarchy structure
         *      ./src/test/java/org/mskcc/crosscheckmetrics/controller/mocks/
         *          \-PROJECT
         */
        final String TEST_PROJECT = "PROJECT";

        Map<String, String> request = new HashMap<>();
        request.put("project", TEST_PROJECT);

        try {
            setupRequest(request);
        } catch (IOException e) {
            log.error(String.format("Error with test setup. %s", e.getMessage()));
            return;
        }
        Map<String, Object> response = crossCheckMetricsController.writeCrosscheckMetrics(TEST_PROJECT);

        // Verify a successful response
        assertEquals("true", response.get("success"));

        verifySavedCrossCheckValues();
    }

    @Test
    public void writeCrosscheckMetrics_fail() {
        /**
         * Names should come from the hierarchy structure
         *      ./src/test/java/org/mskcc/crosscheckmetrics/controller/mocks/
         *          \-RUN
         *              \-PROJECT
         */

        // 1) Bad Header
        final String TEST_RUN = "RUN";
        String testProject = "BAD_PROJECT_HEADER";      // Missing marker for end of metadata lines
        Map<String, String> request = new HashMap<>();
        request.put("project", testProject);
        try {
            setupRequest(request);
        } catch (IOException e) {
            log.error(String.format("Error with test setup. %s", e.getMessage()));
            return;
        }
        Map<String, Object> response = crossCheckMetricsController.writeCrosscheckMetrics(testProject);
        assertEquals("false", response.get("success"));

        // 2) Bad Values
        testProject = "BAD_PROJECT_VALUES";      // Missing all columns for header
        request = new HashMap<>();
        request.put("project", testProject);
        try {
            setupRequest(request);
        } catch (IOException e) {
            log.error(String.format("Error with test setup. %s", e.getMessage()));
            return;
        }
        response = crossCheckMetricsController.writeCrosscheckMetrics(testProject);
        assertEquals("false", response.get("success"));
    }

    private void verifySavedCrossCheckValues() {
        final int numEntries = 9;   // Number of entries in in put file
        verify(crossCheckMetricsRepository, times(numEntries)).save(crosscheckMetricsArgumentCaptor.capture());
        List<CrosscheckMetrics> capturedMetrics = crosscheckMetricsArgumentCaptor.getAllValues();
        assertTrue(capturedMetrics.size() == numEntries);

        Double[] lodScores = new Double[]{
                53.605552,
                32.277395,
                36.303044,
                33.244111,
                56.220436,
                30.956954,
                35.337761,
                28.646621,
                50.528243
        };
        Double[] lodScoreTumorNormal = new Double[]{
                42.817292,
                17.139747,
                27.896047,
                29.998769,
                45.306677,
                29.664082,
                27.235856,
                -3.591111,
                41.098012
        };
        Double[] lodScoreNormalTumor = new Double[]{
                42.817292,
                29.998769,
                27.235856,
                17.139747,
                45.306677,
                -3.591111,
                27.896047,
                29.664082,
                41.098012
        };
        String[] results = new String[]{
                "EXPECTED_MATCH",
                "UNEXPECTED_MATCH",
                "UNEXPECTED_MATCH",
                "UNEXPECTED_MATCH",
                "EXPECTED_MATCH",
                "UNEXPECTED_MATCH",
                "UNEXPECTED_MATCH",
                "UNEXPECTED_MATCH",
                "EXPECTED_MATCH"
        };
        String[] igoIdAValues = new String[]{
                "04969_N_5",
                "04969_N_5",
                "04969_N_5",
                "04969_N_1",
                "04969_N_1",
                "04969_N_1",
                "04969_N_4",
                "04969_N_4",
                "04969_N_4"
        };

        for (int i = 0; i < numEntries; i++) {
            CrosscheckMetrics metric = capturedMetrics.get(i);
            assertEquals(metric.lodScore, lodScores[i]);
            assertEquals(metric.lodScoreTumorNormal, lodScoreTumorNormal[i]);
            assertEquals(metric.lodScoreNormalTumor, lodScoreNormalTumor[i]);
            assertEquals(metric.result, results[i]);
            assertEquals(metric.result, results[i]);
            assertEquals(metric.getId().getIgoIdA(), igoIdAValues[i]);
        }
    }
}
