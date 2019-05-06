package org.mskcc.picardstats.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mskcc.picardstats.model.AlignmentSummaryMetrics.Category;

class AlignmentSummaryMetricsTest {

    @Test
    // SECOND_OF_PAIR	99865176	99865176	1	0	99305003	0.994391	9902628093	89641074	8966181081	8808112282	0	0.003131	0.002825	0.000109	101	99261598	0.999563	00.501777	0.009383	0.000001
    void parseLine() {
        String line = "PAIR\t199730352\t199730352\t1\t0\t198846204\t0.995573\t19846914161\t179871268\t17996675540\t17682453410\t0\t0.002934\t0.002661\t0.000109\t101\t198523196\t0.998376\t0\t0.501117\t0.009383\t0.000001";
        AlignmentSummaryMetrics am = AlignmentSummaryMetrics.parseLine(line, "", null);
        assertEquals(101, am.MEAN_READ_LENGTH);

        String line2 = "SECOND_OF_PAIR\t439205677\t439205677\t1\t0\t438567131\t0.998546\t65602540578\t419783248\t62939721194\t61441242394\t0\t0.004006\t0.003556\t0.000204\t150.720584\t438514544\t0.99988\t0\t0.500209\t0.016767\t0";
        AlignmentSummaryMetrics am2 = AlignmentSummaryMetrics.parseLine(line2, "", null);
        assertEquals(Category.SECOND_OF_PAIR, am2.CATEGORY);
        assertEquals(0.016767d, am2.PCT_CHIMERAS);
    }
}