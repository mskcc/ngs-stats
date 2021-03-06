package org.mskcc.picardstats.repository;

import org.mskcc.picardstats.model.PicardFile;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface PicardFileRepository extends CrudRepository<PicardFile, String> {

    List<PicardFile> findByRequest(String request);

    List<PicardFile> findByRunAndRequest(String run, String request);

    List<PicardFile> findByRun(String run);

    List<PicardFile> findByLastModifiedGreaterThan(Date date);

    @Query(value = "SELECT DISTINCT FILETYPE FROM PICARDFILE WHERE RUN = :run AND REQUEST = :request", nativeQuery =
            true)
    List<String> findFiletypeByRunAndRequest(@Param("run") String run, @Param("request") String request);

    @Query(value = "SELECT DISTINCT RUN FROM PICARDFILE WHERE lastModified BETWEEN DATE_ADD(CURDATE(), INTERVAL -3 DAY) AND DATE_ADD(CURDATE(), INTERVAL -0 DAY)", nativeQuery = true)
    List<String> findRecentRuns();

    @Query(value = "SELECT DISTINCT REQUEST FROM PICARDFILE WHERE lastModified BETWEEN DATE_ADD(CURDATE(), INTERVAL -3 DAY) AND DATE_ADD(CURDATE(), INTERVAL -0 DAY)", nativeQuery = true)
    List<String> findRecentRequests();

    // Note that the MD file is not always generated for non pooled normals - 116k AM files vs 111k MD files
    @Query(value = "SELECT DISTINCT PF.RUN, PF.REQUEST, PF.SAMPLE, PF.REFERENCEGENOME, PF.MD5RRS, AM.PCT_ADAPTER, AM" +
            ".CATEGORY, MD.READ_PAIRS_EXAMINED, MD.UNMAPPED_READS, MD.PERCENT_DUPLICATION, WGS.MEAN_COVERAGE, WGS" +
            ".PCT_10X, WGS.PCT_30X, WGS.PCT_100X \n" +
            "FROM  PICARDFILE PF, ALIGNMENTSUMMARYMETRICS AM, DUPLICATIONMETRICS MD, WGSMETRICS WGS  \n" +
            "WHERE PF.MD5RRS = MD.MD5RRS AND PF.MD5RRS = AM.MD5RRS AND PF.MD5RRS = WGS.MD5RRS AND PF.RUN = :run AND " +
            "PF.REQUEST = 'POOLEDNORMALS'",
            nativeQuery = true)
    List<Object[]> findWGSStatsForPooledNormals(@Param("run") String run);


    List<PicardFile> findByRunAndSample(String runId, String sampleId);
}