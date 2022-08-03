package org.mskcc.picardstats.model;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PicardFileTest {

    @Test
    void getVersionDRAGEN() {
        String result = PicardFile.getVersionDRAGEN("#DRAGEN_VERSION_01.003.044.3.10.1-183-g9ced7ae8 FILENAME");
        assertEquals("DRAGEN_01.003.044.3.10.1-183-g9ced7ae8", result);
    }
    @Test
    //DIANA_0008_AH3V2JDMXX___P07951_I___P-0005083-N01-WES_IGO_07951_I_11___hg19___MD.txt'
    void fromFile() throws IOException {
        File tempFile = File.createTempFile("DIANA_0008_AH3V2JDMXX___P07951_I___P-0005083-N01-WES_IGO_07951_I_11___hg19___MD", ".txt");
        tempFile.deleteOnExit();

        System.out.println("Created temp. file:" + tempFile.getName());
        PicardFile f = PicardFile.fromFile(tempFile);
        assertEquals("DIANA_0008_AH3V2JDMXX", f.getRun());
        assertEquals("07951_I", f.getRequest());
        assertEquals("P-0005083-N01-WES_IGO_07951_I_11", f.getSample());
        assertEquals("hg19", f.getReferenceGenome());
//        //assertEquals("MD", sf.fileType); // does not work with Java temp files
        System.out.println("File created:" + f.getLastModified());
    }

    @Test
    void fromFileWithVersion() throws IOException {
        File tempFile = File.createTempFile("DIANA_0008_AH3V2JDMXX___P07951_I___P-0005083-N01-WES_IGO_07951_I_11___hg19___2_21_2___MD", ".txt");
        tempFile.deleteOnExit();

        System.out.println("Created temp. file:" + tempFile.getName());
        PicardFile f = PicardFile.fromFile(tempFile);
        assertEquals("DIANA_0008_AH3V2JDMXX", f.getRun());
        assertEquals("07951_I", f.getRequest());
        assertEquals("P-0005083-N01-WES_IGO_07951_I_11", f.getSample());
        assertEquals("hg19", f.getReferenceGenome());
//        //assertEquals("MD", sf.fileType); // does not work with Java temp files
        assertEquals("2_21_2", f.getPicardVersion());
        System.out.println("File created:" + f.getLastModified());
    }
}