package org.mskcc.picardstats.repository;

import org.mskcc.picardstats.model.QMetric;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface QMetricsRepository extends CrudRepository<QMetric, Integer> {
    List<QMetric> findByFilename(String filename);

}