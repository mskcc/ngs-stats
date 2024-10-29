package org.mskcc.cellranger.repository;

import org.mskcc.cellranger.model.TenxVisiumHdFfpe;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TenxVisiumHdFfpeRepository extends CrudRepository<TenxVisiumHdFfpe, String> {
    List<TenxVisiumHdFfpe> findByRunId(String runId);
}