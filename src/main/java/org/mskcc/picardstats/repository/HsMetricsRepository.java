package org.mskcc.picardstats.repository;

import org.mskcc.picardstats.model.HsMetrics;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface HsMetricsRepository extends CrudRepository<HsMetrics, Integer> {
    List<HsMetrics> findByFilename(String filename);
}