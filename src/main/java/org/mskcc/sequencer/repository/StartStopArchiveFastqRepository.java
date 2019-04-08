package org.mskcc.sequencer.repository;

import org.mskcc.sequencer.model.StartStopArchiveFastq;
import org.springframework.data.repository.CrudRepository;

public interface StartStopArchiveFastqRepository extends CrudRepository<StartStopArchiveFastq, String> {
}