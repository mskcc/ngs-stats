package org.mskcc.cellranger.repository;

import org.mskcc.cellranger.model.TenxArc;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TenxArcRepository extends CrudRepository<TenxArc, String> {
    List<TenxArc> findByRunId(String runId);
}
