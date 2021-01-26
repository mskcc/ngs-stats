package org.mskcc.permissions.repository;

import org.mskcc.permissions.model.LabMember;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LabMemberRepository extends CrudRepository<LabMember, String> {
    List<LabMember> findByPi(String pi);
}