package org.mskcc.cellranger.repository;

import org.mskcc.cellranger.model.TenxBcr;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TenxBcrRepository extends CrudRepository<TenxBcr, String> {
    List<TenxBcr> findByRunId(String runId);
}