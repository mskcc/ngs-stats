package org.mskcc.sequencer.repository;

import org.mskcc.sequencer.model.ArchivedFastq;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface ArchivedFastqRepository extends CrudRepository<ArchivedFastq, String> {
    // Returns all pooled normals included on the given request ID
    @Query(value = "SELECT * FROM ARCHIVEDFASTQ WHERE project = 'POOLEDNORMALS' AND RUN IN " +
            "(SELECT DISTINCT RUN FROM ARCHIVEDFASTQ E WHERE project = :project)", nativeQuery = true)
    List<ArchivedFastq> findAllPooledNormals(@Param("project") String project);
    List<ArchivedFastq> findByRunStartsWithAndSampleOrderByFastqLastModifiedDesc(String run, String sample);
    List<ArchivedFastq> findBySampleStartsWith(String sample);
    List<ArchivedFastq> findBySampleEndsWith(String sample);
    // Note: projects in the database are often missing the leading '0' so 03595 may be in stored as 3595
    List<ArchivedFastq> findByProject(String project);

    @Query(value = "SELECT DISTINCT PROJECT FROM ARCHIVEDFASTQ WHERE PROJECT IS NOT null AND LASTUPDATED > :startDate",
            nativeQuery = true)
    List<String> findRecentlyArchived(@Param("startDate") Date startDate);
}