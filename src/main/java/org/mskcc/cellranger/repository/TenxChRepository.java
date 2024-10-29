package org.mskcc.cellranger.repository;

import org.mskcc.cellranger.model.TenxCh;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TenxChRepository extends CrudRepository<TenxCh, String> {
    List<TenxCh> findByRunId(String runId);
}