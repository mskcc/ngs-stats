package org.mskcc.cellranger.repository;

import org.mskcc.cellranger.model.TenxAbc;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TenxAbcRepository extends CrudRepository<TenxAbc, String> {
    List<TenxAbc> findByRunId(String runId);
}
