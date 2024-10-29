package org.mskcc.cellranger.repository;

import org.mskcc.cellranger.model.TenxVisiumFfpe;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TenxVisiumFfpeRepository extends CrudRepository<TenxVisiumFfpe, String> {
    List<TenxVisiumFfpe> findByRunId(String runId);
}