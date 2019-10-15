package org.mskcc.cellranger.repository;

import org.mskcc.cellranger.model.CellRangerSummaryVdj;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CellRangerSummaryVdjRepository extends CrudRepository<CellRangerSummaryVdj, String> {
    List<CellRangerSummaryVdj> findByProject(String project);

}
