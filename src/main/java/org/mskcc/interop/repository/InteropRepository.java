package org.mskcc.interop.repository;

import org.mskcc.interop.model.Interop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InteropRepository extends JpaRepository<Interop, String> {
    List<Interop> findByRunId(String runId);
    List<Interop> findByRunIdAndReadNumberAndLane(String runId, Integer readNumber, Integer lane);
    List<Interop> findByRunIdAndReadNumber(String runId, Integer readNumber);
    List<Interop> findByRunIdAndLane(String runId, Integer lane);

}
