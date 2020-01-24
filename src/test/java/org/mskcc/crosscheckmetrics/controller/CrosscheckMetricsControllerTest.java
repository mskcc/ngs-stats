package org.mskcc.crosscheckmetrics.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mskcc.crosscheckmetrics.CrossCheckMetricsController;
import org.mskcc.crosscheckmetrics.model.CrosscheckMetrics;
import org.mskcc.crosscheckmetrics.model.FingerprintResult;
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
import java.util.List;
import java.util.Map;

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
     * @param run
     * @param project
     * @throws IOException
     */
    private void setupWriteCrosscheckMetricsRequest(String run, String project) throws IOException {
        String requestBody = String.format("{\"project\": \"%s\",", project) +
                String.format("\"run\": \"%s\"}]}", run);
        when(request.getInputStream()).thenReturn(
                new DelegatingServletInputStream(
                        new ByteArrayInputStream(requestBody.getBytes(StandardCharsets.UTF_8))));
    }

    @Test
    public void writeCrosscheckMetrics_success() {
        final String TEST_RUN = "RUN";
        final String TEST_PROJECT = "PROJECT";
        try {
            setupWriteCrosscheckMetricsRequest(TEST_RUN, TEST_PROJECT);
        } catch (IOException e) {
            log.error(String.format("Error with test setup. %s", e.getMessage()));
            return;
        }
        Map<String, Object> response = crossCheckMetricsController.writeCrosscheckMetrics(TEST_PROJECT, TEST_RUN);

        // Verify a successful response
        assertEquals("true", response.get("success"));

        verifySavedCrossCheckValues();
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
        Double[] lodAlt1Scores = new Double[]{
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
        Double[] lodAlt2Scores = new Double[]{
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
            assertEquals(metric.lod, lodScores[i]);
            assertEquals(metric.lodAlt1, lodAlt1Scores[i]);
            assertEquals(metric.lodAlt2, lodAlt2Scores[i]);
            assertEquals(metric.result, FingerprintResult.valueOf(results[i]));
            assertEquals(metric.result, FingerprintResult.valueOf(results[i]));
            assertEquals(metric.igoIdA, igoIdAValues[i]);
        }
    }
}
