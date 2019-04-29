package org.mskcc.picardstats;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mskcc.domain.picardstats.PicardStats;
import org.mskcc.picardstats.converter.*;
import org.mskcc.picardstats.model.PicardFile;

import java.util.Date;

public class PicardFileToStatsConverterTest {
    private PicardFileToStatsConverter converter = new PicardFileToStatsConverter(
            new AlignmentStatsConverter(),
            new CpcgStatsConverter(),
            new DuplicationStatsConverter(),
            new HsStatsConverter(),
            new QStatsConverter(),
            new RnaSeqStatsConverter(),
            new WgsStatsConverter()
    );

    @Test
    public void whenPicardFileIsConverted_shouldCreatePicardStats() throws Exception {
        //given
        String filename = "Some_file_name.txt";
        String run = "RUN_ABC_XXXYYY";
        String request = "15432_A";
        String sample = "Some_samplename";
        String refGenome = "ABCDE";
        String fileType = "MD";
        Date lastMod = new Date(2018, 05, 05);
        boolean parseOk = true;
        String md5RRS = "543543FDF";

        PicardFile picardFile = new PicardFile(filename, run, request, sample, refGenome, fileType, lastMod, parseOk);
        picardFile.setMd5RRS(md5RRS);

        //when
        PicardStats picardStats = converter.convert(picardFile);

        //then
        Assertions.assertThat(picardStats.getFilename()).isEqualTo(filename);
        Assertions.assertThat(picardStats.getRun()).isEqualTo(run);
        Assertions.assertThat(picardStats.getSample()).isEqualTo(sample);
        Assertions.assertThat(picardStats.getFileType()).isEqualTo(fileType);
        Assertions.assertThat(picardStats.getLastModified()).isEqualTo(lastMod);
        Assertions.assertThat(picardStats.getRequest()).isEqualTo(request);
        Assertions.assertThat(picardStats.isParseOK()).isEqualTo(parseOk);
        Assertions.assertThat(picardStats.getMd5RRS()).isEqualTo(md5RRS);
    }
}