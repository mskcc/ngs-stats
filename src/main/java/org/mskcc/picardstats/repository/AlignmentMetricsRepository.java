package org.mskcc.picardstats.repository;

import org.mskcc.picardstats.model.AlignmentSummaryMetrics;
import org.springframework.data.repository.CrudRepository;

public interface AlignmentMetricsRepository extends CrudRepository<AlignmentSummaryMetrics, Integer> {
}
