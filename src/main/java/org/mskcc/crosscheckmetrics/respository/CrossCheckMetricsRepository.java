package org.mskcc.crosscheckmetrics.respository;

import org.mskcc.crosscheckmetrics.model.CrosscheckMetrics;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CrossCheckMetricsRepository extends CrudRepository<CrosscheckMetrics, String> {
    List<CrosscheckMetrics> findByProject(String project);
}
