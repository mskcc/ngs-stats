package org.mskcc.picardstats.repository;

import org.mskcc.picardstats.model.HsMetrics;
import org.springframework.data.repository.CrudRepository;

public interface HsMetricsRepository extends CrudRepository<HsMetrics, Integer> {
}
