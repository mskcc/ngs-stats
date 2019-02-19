package org.mskcc.picardstats.repository;

import org.mskcc.picardstats.model.RnaSeqMetrics;
import org.springframework.data.repository.CrudRepository;

public interface RnaSeqMetricsRepository extends CrudRepository<RnaSeqMetrics, Integer> {
}
