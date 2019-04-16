package org.mskcc.sequencer.repository;

import org.mskcc.sequencer.model.ArchivedFastq;
import org.springframework.data.repository.CrudRepository;

public interface ArchivedFastqRepository extends CrudRepository<ArchivedFastq, String> {
}