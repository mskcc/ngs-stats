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
import java.util.*;

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
    private static String SAMPLE_COUNT;
    private static String SAMPLE_VDJ;

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

    private static Map<String,Path> WEB_SUMMARY_PATH = new HashMap<>();

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

    /**
     * Sets up Test
     *      1) application.properties - [ WEB_SUMMARY_PATH ]
     *      2) Adds web_summary.html files,
     *          e.g.
     *              /var/folders/x7/5dxp7t9573d7jyx8mv1g8zx1mwnqpr/T/mockCellRanger.../run.../project..
     *              ├── sample...__count
     *                  └── outs
     *                      └── web_summary.html.
     *              └── sample...__vdj
     *                  └── outs
     *                      └── web_summary.html.
     *
     * @throws IOException
     */
    @Before
    public void createMockCellRangerDir() throws IOException {
        MockitoAnnotations.initMocks(this);

        // Setup application.properties
        ReflectionTestUtils.setField(cellRangerController, "WEB_SUMMARY_PATH", "outs/web_summary.html");

        // Create web_summary.html files
        String tmpDir = System.getProperty("java.io.tmpdir");   // e.g. /var/folders/x7/5dxp7t9573d7jyx8mv1g8zx1mwnqpr/T/
        CELL_RANGER_DIR = Files.createTempDirectory(Paths.get(tmpDir), "mockCellRanger");
        ReflectionTestUtils.setField(cellRangerController, "CELL_RANGER_DIR", CELL_RANGER_DIR.toString());

        Path currPath = CELL_RANGER_DIR;
        currPath = Files.createTempDirectory(currPath, "run");
        RUN = currPath.getFileName().toString();
        currPath = Files.createTempDirectory(currPath, "project");
        PROJECT = currPath.getFileName().toString();
        Path samplePath = Files.createTempDirectory(currPath, "sample");
        SAMPLE = samplePath.getFileName().toString();

        String[] types = new String[]{"vdj", "count"};
        Path typePath;
        for(String type : types){
            typePath = createTypeDirectories(samplePath,type);
            WEB_SUMMARY_PATH.put(type, typePath);
        }
    }

    /**
     * Given the current directory for a sample in tmp directory, create the corresponding sample__type folder
     * structure that mimics how the web_summary.html files are saved
     *
     * E.g.
     *      INPUT:
     *          path: /var/folders/x7/5dxp7t9573d7jyx8mv1g8zx1mwnqpr/T/mockCellRanger.../run.../project../sample...
     *          type: count
     *      (Create):
     *      /var/folders/x7/5dxp7t9573d7jyx8mv1g8zx1mwnqpr/T/mockCellRanger.../run.../project../sample...
     *          ├── sample...__[TYPE]
     *              └── outs
     *                  └── web_summary.html
     *      RETURN: Path(/PATH/TO/run.../project../sample...__count/outs/web_summary.html)
     *
     * @param samplePath, Path
     * @param type, String - CellRanger type of output
     * @return
     * @throws IOException
     */
    private Path createTypeDirectories(Path samplePath, String type) throws IOException {
        Path runPath = samplePath.getParent();
        String sampleType = String.format("%s__%s", samplePath.toString(), type);
        Path tmpPath = Files.createTempDirectory(runPath, "tmp");
        File typeDir = new File(sampleType);

        tmpPath.toFile().renameTo(typeDir);
        Path typePath = Paths.get(sampleType);

        Path outPathTmp = Files.createTempDirectory(typePath,"outs");
        String outPathParent = outPathTmp.getParent().toString();
        String outPath = String.format("%s/outs",outPathParent);

        File outDir = new File(outPath);
        outPathTmp.toFile().renameTo(outDir);

        Path webSummaryPath = Files.createTempFile(Paths.get(outPath), "web_summary", ".html");

        String webSummaryName =  String.format("%s/%s", outDir.toString(), "web_summary.html");
        webSummaryPath.toFile().renameTo(new File(webSummaryName));

        return Paths.get(webSummaryName);
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
    public void saveCellRangerSampleTest_success() {
        CellRangerType[] types = getCellRangerTypes();
        for(CellRangerType type : types){
            try {
                testSaveCellRangerSample(type);
            } catch(IOException e){
                log.error(String.format("Failed to test %s. Error: %s", type, e.getMessage()));
            }
        }
    }

    @Test
    public void saveCellRangerSampleTest_fail() {
        CellRangerType[] types = getCellRangerTypes();
        for(CellRangerType type : types){
            try {
                String[] params = new String[]{type.toString(), SAMPLE, PROJECT, RUN};
                String temp;
                for(int i = 0; i<params.length; i++){
                    temp = params[i];
                    params[i] = "INVALID_PARAM";
                    setupSaveRequest(params[0], params[1], params[2], params[3]);
                    Map<String,Object> response = cellRangerController.saveCellRangerSample(request);
                    assertEquals(response.get("success"), "false");

                    params[i] = temp;
                }
            } catch(IOException e){
                log.error(String.format("Failed to test %s. Error: %s", type, e.getMessage()));
            }
        }
    }

    @Test
    public void getCellRangerSampleTest_success() {
        final String project = "REQUEST_PROJECT";

        // Setup database get calls
        CellRangerSummaryVdj mockVdjSample = new CellRangerSummaryVdj();
        mockVdjSample.setField("id", "TEST_VDJ_ID", String.class);
        ArrayList<CellRangerSummaryVdj> vdjSamples = new ArrayList<>(Arrays.asList(mockVdjSample));

        CellRangerSummaryCount mockCountSample = new CellRangerSummaryCount();
        mockCountSample.setField("id", "TEST_COUNT_ID", String.class);
        ArrayList<CellRangerSummaryCount> countSamples = new ArrayList<>(Arrays.asList(mockCountSample));

        when(cellRangerSummaryVdjRepository.findByProject(project)).thenReturn(vdjSamples);
        when(cellRangerSummaryCountRepository.findByProject(project)).thenReturn(countSamples);

        CellRangerType[] types = getCellRangerTypes();
        for(CellRangerType type : types){
            Map<String,Object> response = cellRangerController.getCellRangerSample(project, type.toString());
            assertEquals(response.get("success"), "true");
            assertNotNull(response.get("data"));
        }
    }

    @Test
    public void getCellRangerSampleTest_fail() {
        final String project = "REQUEST_PROJECT";

        List<CellRangerSummaryVdj> vdjEmptyList = new ArrayList<>();
        List<CellRangerSummaryCount> countEmptyList = new ArrayList<>();

        // Setup database get calls
        when(cellRangerSummaryVdjRepository.findByProject(project)).thenReturn(vdjEmptyList);
        when(cellRangerSummaryCountRepository.findByProject(project)).thenReturn(countEmptyList);

        CellRangerType[] types = getCellRangerTypes();
        for(CellRangerType type : types){
            Map<String,Object> response = cellRangerController.getCellRangerSample(project, type.toString());
            assertEquals(response.get("success"), "false");
            assertNull(response.get("data"));
        }
    }

    /**
     * Return all available cell ranger data types
     * @return
     */
    private CellRangerType[] getCellRangerTypes() {
        CellRangerType[] types = new CellRangerType[2];
        types[0] = CellRangerType.COUNT;
        types[1] = CellRangerType.VDJ;

        return types;
    }

    /**
     * Creates request body as it would be sent in request. Mocks it as the input stream of the HttpServletRequest
     *
     * @param type, String
     * @param sample, String
     * @param project, String
     * @param run, String
     * @throws IOException
     */
    private void setupSaveRequest(String type, String sample, String project, String run) throws IOException {
        String requestBody = String.format("{\"samples\": [{\"sample\": \"%s\",", sample) +
                String.format("\"type\": \"%s\",", type) +
                String.format("\"project\": \"%s\",", project) +
                String.format("\"run\": \"%s\"}]}", run);
        when(request.getInputStream()).thenReturn(
                new DelegatingServletInputStream(
                        new ByteArrayInputStream(requestBody.getBytes(StandardCharsets.UTF_8))));
    }

    private void testSaveCellRangerSample(CellRangerType type) throws IOException {
        copyFileUsingStream(getMockWebSummaryFile(type), WEB_SUMMARY_PATH.get(type.toString()).toFile());
        setupSaveRequest(type.toString(), SAMPLE, PROJECT, RUN);

        Map<String,Object> response = cellRangerController.saveCellRangerSample(request);
        assertEquals("true", response.get("success"));

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

        assertTrue(cellRangerSummaryVdj.EstimatedNumberOfCells == 2464D);
        assertTrue(cellRangerSummaryVdj.MeanReadsPerCell == 14900D);
        assertTrue(cellRangerSummaryVdj.NumCellsWithVDJSpanningPair == 1958D);
        assertTrue(cellRangerSummaryVdj.ReadsMappedToAnyVDJGene == 0.831D);
        assertTrue(cellRangerSummaryVdj.ReadsMappedToTRA == 0.269D);
        assertTrue(cellRangerSummaryVdj.ReadsMappedToTRB == 0.464D);
        assertTrue(cellRangerSummaryVdj.MedianTRAUMIsPerCell == 3D);
        assertTrue(cellRangerSummaryVdj.MedianTRBUMIsPerCell == 9D);
        assertTrue(cellRangerSummaryVdj.NumCellsWithVDJSpanningPair == 1958D);
        assertTrue(cellRangerSummaryVdj.NumberOfReadPairs == 36715767D);
        assertTrue(cellRangerSummaryVdj.ValidBarcodes == 0.961D);
        assertTrue(cellRangerSummaryVdj.Q30BasesInBarcode == 0.959D);
        assertTrue(cellRangerSummaryVdj.Q30BasesInRNARead1 == 0.944D);
        assertTrue(cellRangerSummaryVdj.Q30BasesInRNARead2 == 0.924D);
        assertTrue(cellRangerSummaryVdj.Q30BasesInSampleIndex == 0.891D);
        assertTrue(cellRangerSummaryVdj.Q30BasesInUMI == 0.951D);
        assertTrue(cellRangerSummaryVdj.Name.equals("TCRbp-ctl1-5f_IGO_09987_B_1"));
        assertTrue(cellRangerSummaryVdj.VDJReference.equals("vdj_GRCm38_alts_ensembl"));
        assertTrue(cellRangerSummaryVdj.Chemistry.equals("Single Cell V(D)J"));
        assertTrue(cellRangerSummaryVdj.CellRangerVersion.equals("3.0.2"));
    }

    private void testCountSummaryContent(){
        verify(cellRangerSummaryCountRepository).save(cellRangerSummaryCountCaptor.capture());
        CellRangerSummaryCount cellRangerSummaryCount = cellRangerSummaryCountCaptor.<CellRangerSummaryCount>getValue();

        assertThat(cellRangerSummaryCount).isNotNull();

        assertTrue(cellRangerSummaryCount.EstimatedNumberOfCells == 3457D);
        assertTrue(cellRangerSummaryCount.MeanReadsPerCell == 54846D);
        assertTrue(cellRangerSummaryCount.MedianGenesPerCell == 2956D);
        assertTrue(cellRangerSummaryCount.NumberOfReads == 189603485D);
        assertTrue(cellRangerSummaryCount.ValidBarcodes == 0.977D);
        assertTrue(cellRangerSummaryCount.SequencingSaturation == 0.371D);
        assertTrue(cellRangerSummaryCount.Q30BasesInBarcode == 0.952D);
        assertTrue(cellRangerSummaryCount.Q30BasesinRNARead == 0.926D);
        assertTrue(cellRangerSummaryCount.Q30BasesInUMI == 0.951D);
        assertTrue(cellRangerSummaryCount.ReadsMappedToGenome == 0.971D);
        assertTrue(cellRangerSummaryCount.ReadsMappedConfidentlyToGenome == 0.938D);
        assertTrue(cellRangerSummaryCount.ReadsMappedToIntergenicRegions == 0.082D);
        assertTrue(cellRangerSummaryCount.ReadsMappedToIntronicRegions == 0.194D);
        assertTrue(cellRangerSummaryCount.ReadsMappedToExonicRegions == 0.662D);
        assertTrue(cellRangerSummaryCount.ReadsMappedToTranscriptome == 0.628D);
        assertTrue(cellRangerSummaryCount.ReadsMappedAntisenseToGene == 0.012D);
        assertTrue(cellRangerSummaryCount.FractionReadsInCells == 0.915D);
        assertTrue(cellRangerSummaryCount.TotalGenesDetected == 23744D);
        assertTrue(cellRangerSummaryCount.MedianUMICountsPerCell == 11512D);
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
                return new File(String.format("%s/sample__count/outs/web_summary.html", path));
            case VDJ:
                return new File(String.format("%s/sample__vdj/outs/web_summary.html", path));
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