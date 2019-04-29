package org.mskcc.picardstats.converter;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mskcc.domain.picardstats.QStats;
import org.mskcc.picardstats.model.QMetric;


public class QStatsConverterTest {
    @Test
    public void whenQMetricsreConverted_shouldReturnQStats() throws Exception {
        //given
        QStatsConverter converter = new QStatsConverter();
        QMetric qMetric = new QMetric();
        qMetric.filename = "gsgsdfds.txt";
        qMetric.md5RRS = "43tgreg";
        qMetric.mskQ = 432;

        //when
        QStats qStats = converter.convert(qMetric);

        //then
        Assertions.assertThat(qStats.filename).isEqualTo(qMetric.filename);
        Assertions.assertThat(qStats.md5RRS).isEqualTo(qMetric.md5RRS);
        Assertions.assertThat(qStats.mskQ).isEqualTo(qMetric.mskQ);
    }

}