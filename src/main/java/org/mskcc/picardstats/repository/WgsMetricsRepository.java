package org.mskcc.picardstats.repository;

import org.mskcc.picardstats.model.WgsMetrics;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface WgsMetricsRepository extends CrudRepository<WgsMetrics, Integer> {
    List<WgsMetrics> findByFilename(String filename);
}