package org.mskcc.picardstats.repository;

import org.mskcc.picardstats.model.RnaSeqMetrics;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RnaSeqMetricsRepository extends CrudRepository<RnaSeqMetrics, Integer> {
    List<RnaSeqMetrics> findByFilename(String filename);

}
