package org.mskcc.picardstats.repository;

import org.mskcc.picardstats.model.DuplicationMetrics;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DuplicationMetricsRepository extends CrudRepository<DuplicationMetrics, Integer> {
    List<DuplicationMetrics> findByFilename(String filename);
}