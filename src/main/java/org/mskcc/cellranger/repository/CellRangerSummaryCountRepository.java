package org.mskcc.cellranger.repository;

import org.mskcc.cellranger.model.CellRangerSummaryCount;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CellRangerSummaryCountRepository extends CrudRepository<CellRangerSummaryCount, String> {
    List<CellRangerSummaryCount> findByProject(String project);
}
