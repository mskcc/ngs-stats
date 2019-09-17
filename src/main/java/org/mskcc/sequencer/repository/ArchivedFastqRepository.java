package org.mskcc.sequencer.repository;

import org.mskcc.sequencer.model.ArchivedFastq;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ArchivedFastqRepository extends CrudRepository<ArchivedFastq, String> {
    List<ArchivedFastq> findByRunStartsWithAndSampleOrderByFastqLastModifiedDesc(String run, String sample);
    List<ArchivedFastq> findBySampleStartsWith(String sample);
}