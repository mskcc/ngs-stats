package org.mskcc.cellranger.repository;

import org.springframework.data.repository.CrudRepository;
import org.mskcc.cellranger.model.CellRangerSummaryCount;

public interface CellRangerSummaryCountRepository extends CrudRepository<CellRangerSummaryCount, String>{ }
