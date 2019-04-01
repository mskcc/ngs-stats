package org.mskcc.sequencer.repository;

import org.mskcc.sequencer.model.StartStopSequencer;
import org.springframework.data.repository.CrudRepository;

public interface StartStopSequencerRepository extends CrudRepository<StartStopSequencer, String> {
}