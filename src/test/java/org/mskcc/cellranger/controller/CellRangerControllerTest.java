package org.mskcc.cellranger.controller;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mskcc.cellranger.model.CellRangerSummaryCount;
import org.mskcc.cellranger.model.CellRangerSummaryVdj;
import org.mskcc.cellranger.repository.CellRangerSummaryCountRepository;
import org.mskcc.cellranger.repository.CellRangerSummaryVdjRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.DelegatingServletInputStream;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CellRangerControllerTest {
    private static Logger log = LoggerFactory.getLogger(CellRangerControllerTest.class);

    private static Path CELL_RANGER_DIR;
    private static String PROJECT;
    private static String SAMPLE;
    private static String RUN;

    @Captor
    ArgumentCaptor<CellRangerSummaryCount> cellRangerSummaryCountCaptor;

    @Captor
    ArgumentCaptor<CellRangerSummaryVdj> cellRangerSummaryVdjCaptor;

    @InjectMocks
    private CellRangerController cellRangerController;

    @Mock
    private CellRangerSummaryCountRepository cellRangerSummaryCountRepository;

    @Mock
    private CellRangerSummaryVdjRepository cellRangerSummaryVdjRepository;

    @Mock
    HttpServletRequest request;

    private static Path WEB_SUMMARY_PATH;

    private enum CellRangerType {
        VDJ("vdj"),
        COUNT("count");

        private final String name;
        CellRangerType(String s) {
            name = s;
        }

        public String toString() {
            return this.name;
        }
    }

    @Before
    public void createMockCellRangerDir() throws IOException {
        MockitoAnnotations.initMocks(this);

        final String samplePath = "project/run/sample";
        final String webSummaryFile = "web_summary";

        String tmpDir = System.getProperty("java.io.tmpdir");

        CELL_RANGER_DIR = Files.createTempDirectory(Paths.get(tmpDir), "mockCellRanger");
        ReflectionTestUtils.setField(cellRangerController, "CELL_RANGER_COUNT_DIR", CELL_RANGER_DIR.toString());
        ReflectionTestUtils.setField(cellRangerController, "CELL_RANGER_VDJ_DIR", CELL_RANGER_DIR.toString());

        Path currPath = CELL_RANGER_DIR;
        currPath = Files.createTempDirectory(currPath, "project");
        PROJECT = currPath.getFileName().toString();
        currPath = Files.createTempDirectory(currPath, "run");
        RUN = currPath.getFileName().toString();
        currPath = Files.createTempDirectory(currPath, "sample");
        SAMPLE = currPath.getFileName().toString();

        Path webSummaryPath = Files.createTempFile(currPath, "web_summary", ".html");
        WEB_SUMMARY_PATH = webSummaryPath;
        ReflectionTestUtils.setField(cellRangerController, "WEB_SUMMARY_PATH", WEB_SUMMARY_PATH.getFileName().toString());
    }

    @After
    public void deleteMockCellRangerDir() throws IOException {
        if(CELL_RANGER_DIR != null){
            Files.walk(CELL_RANGER_DIR)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        }
    }

    @Test
    public void updateDatabaseFileFilterTest() {
        CellRangerType[] types = new CellRangerType[2];
        types[0] = CellRangerType.COUNT;
        types[1] = CellRangerType.VDJ;

        for(CellRangerType type : types){
            try {
                testSaveCellRangerSample(type);
            } catch(IOException e){
                log.error(String.format("Failed to test %s. Error: %s", type, e.getMessage()));
            }
        }
    }

    private void testSaveCellRangerSample(CellRangerType type) throws IOException {
        copyFileUsingStream(getMockWebSummaryFile(type), WEB_SUMMARY_PATH.toFile());
        String requestBody = String.format("{\"sample\": \"%s\",", SAMPLE) +
                String.format("\"type\": \"%s\",", type.toString()) +
                String.format("\"project\": \"%s\",", PROJECT) +
                String.format("\"run\": \"%s\"}", RUN);
        when(request.getInputStream()).thenReturn(
                new DelegatingServletInputStream(
                        new ByteArrayInputStream(requestBody.getBytes(StandardCharsets.UTF_8))));
        Map<String,String> response = cellRangerController.saveCellRangerSample(request);
        assertEquals(response.get("success"), "true");

        if(type.equals(CellRangerType.COUNT)){
            testCountSummaryContent();
        } else if(type.equals(CellRangerType.VDJ)){
            testVdjSummaryContent();
        } else {
            fail("Invalid type for testing");
        }
    }

    private void testVdjSummaryContent(){
        verify(cellRangerSummaryVdjRepository).save(cellRangerSummaryVdjCaptor.capture());
        CellRangerSummaryVdj cellRangerSummaryVdj = cellRangerSummaryVdjCaptor.<CellRangerSummaryVdj>getValue();

        assertThat(cellRangerSummaryVdj).isNotNull();

        assertTrue(cellRangerSummaryVdj.EstimatedNumberOfCells == 2464);
        assertTrue(cellRangerSummaryVdj.MeanReadsPerCell == 14900);
        assertTrue(cellRangerSummaryVdj.NumCellsWithVDJSpanningPair == 1958);
        assertTrue(cellRangerSummaryVdj.ReadsMappedToAnyVDJGene == 0.831F);
        assertTrue(cellRangerSummaryVdj.ReadsMappedToTRA == 0.269F);
        assertTrue(cellRangerSummaryVdj.ReadsMappedToTRB == 0.464F);
        assertTrue(cellRangerSummaryVdj.MedianTRAUMIsPerCell == 3);
        assertTrue(cellRangerSummaryVdj.MedianTRBUMIsPerCell == 9);
        assertTrue(cellRangerSummaryVdj.NumCellsWithVDJSpanningPair == 0.795F);
        assertTrue(cellRangerSummaryVdj.NumberOfReadPairs == 36715767);
        assertTrue(cellRangerSummaryVdj.ValidBarcodes == 0.961F);
        assertTrue(cellRangerSummaryVdj.Q30BasesInBarcode == 0.959);
        assertTrue(cellRangerSummaryVdj.Q30BasesInRNARead1 == 0.944);
        assertTrue(cellRangerSummaryVdj.Q30BasesInRNARead2 == 0.924);
        assertTrue(cellRangerSummaryVdj.Q30BasesInSampleIndex == 0.891);
        assertTrue(cellRangerSummaryVdj.Q30BasesInUMI == 0.951);
        assertTrue(cellRangerSummaryVdj.Name.equals("TCRbp-ctl1-5f_IGO_09987_B_1"));
        assertTrue(cellRangerSummaryVdj.VDJReference.equals("vdj_GRCm38_alts_ensembl"));
        assertTrue(cellRangerSummaryVdj.Chemistry.equals("Single Cell V(D)J"));
        assertTrue(cellRangerSummaryVdj.CellRangerVersion.equals("3.0.2"));
    }

    private void testCountSummaryContent(){
        verify(cellRangerSummaryCountRepository).save(cellRangerSummaryCountCaptor.capture());
        CellRangerSummaryCount cellRangerSummaryCount = cellRangerSummaryCountCaptor.<CellRangerSummaryCount>getValue();

        assertThat(cellRangerSummaryCount).isNotNull();

        assertTrue(cellRangerSummaryCount.EstimatedNumberOfCells == 3457);
        assertTrue(cellRangerSummaryCount.MeanReadsPerCell == 54846);
        assertTrue(cellRangerSummaryCount.MedianGenesPerCell == 2956);
        assertTrue(cellRangerSummaryCount.NumberOfReads == 189603485);
        assertTrue(cellRangerSummaryCount.ValidBarcodes == 0.977F);
        assertTrue(cellRangerSummaryCount.SequencingSaturation == 0.371F);
        assertTrue(cellRangerSummaryCount.Q30BasesInBarcode == 0.952F);
        assertTrue(cellRangerSummaryCount.Q30BasesinRNARead == 0.926F);
        assertTrue(cellRangerSummaryCount.Q30BasesInUMI == 0.951F);
        assertTrue(cellRangerSummaryCount.ReadsMappedToGenome == 0.971F);
        assertTrue(cellRangerSummaryCount.ReadsMappedConfidentlyToGenome == 0.938F);
        assertTrue(cellRangerSummaryCount.ReadsMappedToIntergenicRegions == 0.082F);
        assertTrue(cellRangerSummaryCount.ReadsMappedToIntronicRegions == 0.194F);
        assertTrue(cellRangerSummaryCount.ReadsMappedToExonicRegions == 0.662F);
        assertTrue(cellRangerSummaryCount.ReadsMappedToTranscriptome == 0.628F);
        assertTrue(cellRangerSummaryCount.ReadsMappedAntisenseToGene == 0.012F);
        assertTrue(cellRangerSummaryCount.FractionReadsInCells == 0.915F);
        assertTrue(cellRangerSummaryCount.TotalGenesDetected == 23744);
        assertTrue(cellRangerSummaryCount.MedianUMICountsPerCell == 11512);
        assertTrue(cellRangerSummaryCount.Name.equals("RUQ_IGO_09443_C_6"));
        assertTrue(cellRangerSummaryCount.Transcriptome.equals("GRCh38"));
        assertTrue(cellRangerSummaryCount.Chemistry.equals("Single Cell 3' v3"));
        assertTrue(cellRangerSummaryCount.CellRangerVersion.equals("3.0.2"));
    }

    private File getMockWebSummaryFile(CellRangerType type){
        final String pkgName = new CellRangerControllerTest().getClass().getPackage().getName();
        final String pkgPath = pkgName.replace(".", "/");
        final String path = String.format("./src/test/java/%s", pkgPath);

        switch(type) {
            case COUNT:
                return new File(String.format("%s/mocks/count_web_summary.html", path));
            case VDJ:
                return new File(String.format("%s/mocks/vdj_web_summary.html", path));
            default:
                break;
        }
        return null;
    }

    /**
     * REF - https://www.journaldev.com/861/java-copy-file
     * @param source
     * @param dest
     * @throws IOException
     */
    private static void copyFileUsingStream(File source, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } catch(Exception e){
            log.error(e.getMessage());
        }finally {
            is.close();
            os.close();
        }
    }
}