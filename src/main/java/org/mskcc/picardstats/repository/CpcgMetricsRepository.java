package org.mskcc.picardstats.repository;

import org.mskcc.picardstats.model.CpcgMetrics;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CpcgMetricsRepository extends CrudRepository<CpcgMetrics, Integer> {
    List<CpcgMetrics> findByFilename(String filename);
}
