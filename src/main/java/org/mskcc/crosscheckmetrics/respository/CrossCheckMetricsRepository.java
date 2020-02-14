package org.mskcc.crosscheckmetrics.respository;

import org.mskcc.crosscheckmetrics.model.CrosscheckMetrics;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CrossCheckMetricsRepository extends CrudRepository<CrosscheckMetrics, String> {
    @Query( "select metric from CrosscheckMetrics metric where project in :projects" )
    List<CrosscheckMetrics> findByCrosscheckMetrics_IdProject_IsIn(List<String> projects);
}
