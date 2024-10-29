package org.mskcc.cellranger.repository;

import org.mskcc.cellranger.model.TenxVisium;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TenxVisiumRepository extends CrudRepository<TenxVisium, String> {
    List<TenxVisium> findByRunId(String runId);
}