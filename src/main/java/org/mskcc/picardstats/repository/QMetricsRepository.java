package org.mskcc.picardstats.repository;

import org.mskcc.picardstats.model.QMetric;
import org.springframework.data.repository.CrudRepository;

public interface QMetricsRepository extends CrudRepository<QMetric, Integer> {
}