package org.mskcc.sequencer.repository;

import org.mskcc.sequencer.model.ArchivedFastq;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ArchivedFastqRepository extends CrudRepository<ArchivedFastq, String> {
    List<ArchivedFastq> findByProjectAndSampleAndRunOrderByFastqLastModifiedDesc(String project, String sample, String run);
}