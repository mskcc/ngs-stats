package org.mskcc.picardstats.repository;

import org.mskcc.picardstats.model.AlignmentSummaryMetrics;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AlignmentMetricsRepository extends CrudRepository<AlignmentSummaryMetrics, Integer> {
    List<AlignmentSummaryMetrics> findByFilename(String filename);
}