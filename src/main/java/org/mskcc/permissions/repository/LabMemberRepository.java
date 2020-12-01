package org.mskcc.permissions.repository;

import org.mskcc.permissions.model.LabMember;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LabMemberRepository extends CrudRepository<LabMember, String> {
    @Query(value = "SELECT member FROM LABMEMBER WHERE pi = :pi)", nativeQuery = true)
    List<String> findByPi(String pi);
}
