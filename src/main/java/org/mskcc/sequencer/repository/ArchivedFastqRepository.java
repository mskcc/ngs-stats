package org.mskcc.sequencer.repository;

import org.mskcc.sequencer.model.ArchivedFastq;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArchivedFastqRepository extends CrudRepository<ArchivedFastq, String> {
    // Returns all pooled normals included on the given request ID
    @Query(value = "SELECT * FROM ARCHIVEDFASTQ WHERE project = 'POOLEDNORMALS' AND RUN IN " +
            "(SELECT DISTINCT RUN FROM ARCHIVEDFASTQ E WHERE project = :project)", nativeQuery = true)
    List<ArchivedFastq> findAllPooledNormals(@Param("project") String project);
    
    List<ArchivedFastq> findByRunStartsWithAndSampleOrderByFastqLastModifiedDesc(String run, String sample);
    List<ArchivedFastq> findBySampleStartsWith(String sample);
    List<ArchivedFastq> findBySampleEndsWith(String sample);
}