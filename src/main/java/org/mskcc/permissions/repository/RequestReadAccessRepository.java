package org.mskcc.permissions.repository;

import org.mskcc.permissions.model.RequestReadAccess;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RequestReadAccessRepository extends CrudRepository<RequestReadAccess, String> {
    List<RequestReadAccess> findByRequest(String request);
}