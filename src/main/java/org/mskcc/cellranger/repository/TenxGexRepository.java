package org.mskcc.cellranger.repository;

import org.mskcc.cellranger.model.TenxGex;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TenxGexRepository extends CrudRepository<TenxGex, String> {
    List<TenxGex> findByRunId(String runId);
}