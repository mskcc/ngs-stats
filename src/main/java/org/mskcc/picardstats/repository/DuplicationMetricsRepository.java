package org.mskcc.picardstats.repository;

import org.mskcc.picardstats.model.DuplicationMetrics;
import org.springframework.data.repository.CrudRepository;

public interface DuplicationMetricsRepository extends CrudRepository<DuplicationMetrics, Integer> {
}
