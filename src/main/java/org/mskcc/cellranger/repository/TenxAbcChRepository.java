package org.mskcc.cellranger.repository;

import org.mskcc.cellranger.model.TenxAbcCh;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TenxAbcChRepository extends CrudRepository<TenxAbcCh, String> {
    List<TenxAbcCh> findByRunId(String runId);
}
