package org.mskcc.picardstats.converter;

import org.mskcc.domain.picardstats.QStats;
import org.mskcc.picardstats.model.QMetric;
import org.springframework.stereotype.Component;

@Component
public class QStatsConverter {
    public QStats convert(QMetric qMetric) {
        QStats qStats = new QStats();

        if (qMetric == null)
            return qStats;

        qStats.setFilename(qMetric.filename);
        qStats.setMd5RRS(qMetric.md5RRS);
        qStats.setMskQ(qMetric.mskQ);

        return qStats;
    }
}
