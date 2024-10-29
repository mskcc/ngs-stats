package org.mskcc.cellranger.repository;

import org.mskcc.cellranger.model.TenxAtac;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TenxAtacRepository extends CrudRepository<TenxAtac, String> {
    List<TenxAtac> findByRunId(String runId);
}
