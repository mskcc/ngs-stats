package org.mskcc.picardstats;

import org.mskcc.domain.picardstats.PicardStats;
import org.mskcc.picardstats.converter.*;
import org.mskcc.picardstats.model.PicardFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PicardFileToStatsConverter {
    private AlignmentStatsConverter alignmentStatsConverter;
    private CpcgStatsConverter cpcgStatsConverter;
    private DuplicationStatsConverter duplicationStatsConverter;
    private HsStatsConverter hsStatsConverter;
    private QStatsConverter qStatsConverter;
    private RnaSeqStatsConverter rnaSeqStatsConverter;
    private WgsStatsConverter wgsStatsConverter;

    @Autowired
    public PicardFileToStatsConverter(AlignmentStatsConverter alignmentStatsConverter, CpcgStatsConverter
            cpcgStatsConverter, DuplicationStatsConverter duplicationStatsConverter, HsStatsConverter
            hsStatsConverter, QStatsConverter qStatsConverter, RnaSeqStatsConverter rnaSeqStatsConverter,
                                      WgsStatsConverter wgsStatsConverter) {
        this.alignmentStatsConverter = alignmentStatsConverter;
        this.cpcgStatsConverter = cpcgStatsConverter;
        this.duplicationStatsConverter = duplicationStatsConverter;
        this.hsStatsConverter = hsStatsConverter;
        this.qStatsConverter = qStatsConverter;
        this.rnaSeqStatsConverter = rnaSeqStatsConverter;
        this.wgsStatsConverter = wgsStatsConverter;
    }

    public PicardStats convert(PicardFile picardFile) {
        PicardStats picardStats = new PicardStats();

        picardStats.setAlignmentStats(alignmentStatsConverter.convert(picardFile.getAlignmentSummaryMetrics()));
        picardStats.setCpcgStats(cpcgStatsConverter.convert(picardFile.getCpcgMetrics()));
        picardStats.setDuplicationStats(duplicationStatsConverter.convert(picardFile.getDuplicationMetrics()));
        picardStats.setHsStats(hsStatsConverter.convert(picardFile.getHsMetrics()));
        picardStats.setQStats(qStatsConverter.convert(picardFile.getQMetric()));
        picardStats.setRnaSeqStats(rnaSeqStatsConverter.convert(picardFile.getRnaSeqMetrics()));
        picardStats.setWgsStats(wgsStatsConverter.convert(picardFile.getWgsMetrics()));

        picardStats.setParseOK(picardFile.isParseOK());
        picardStats.setFileImported(picardFile.getFileImported());
        picardStats.setFilename(picardFile.getFilename());
        picardStats.setFileType(picardFile.getFileType());
        picardStats.setLastModified(picardFile.getLastModified());
        picardStats.setMd5RRS(picardFile.getMd5RRS());
        picardStats.setReferenceGenome(picardFile.getReferenceGenome());
        picardStats.setRequest(picardFile.getRequest());
        picardStats.setRun(picardFile.getRun());
        picardStats.setSample(picardFile.getSample());

        return picardStats;
    }
}
