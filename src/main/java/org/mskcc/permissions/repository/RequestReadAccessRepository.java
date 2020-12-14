package org.mskcc.permissions.repository;

import org.mskcc.permissions.model.LabMember;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RequestReadAccessRepository extends CrudRepository<LabMember, String> {
    @Query(value = "SELECT member FROM requestreadaccess WHERE request = :request", nativeQuery = true)
    List<String> findByRequest(String request);
}