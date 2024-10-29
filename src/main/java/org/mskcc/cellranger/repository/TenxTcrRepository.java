package org.mskcc.cellranger.repository;

import org.mskcc.cellranger.model.TenxTcr;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TenxTcrRepository extends CrudRepository<TenxTcr, String> {
    List<TenxTcr> findByRunId(String runId);
}