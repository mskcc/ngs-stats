package org.mskcc.permissions.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.mskcc.permissions.model.LabMember;
import org.mskcc.permissions.model.RequestReadAccess;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class RequestPermissions {
    private String labName;
    private List<LabMember> labMembers;

    private String request;
    private List<RequestReadAccess> requestReadAccess;  // individuals who have access to this specific request
    // groups that have read access such as 'cmoigo', 'bicigo', 'isabl' with special code in LIMS or iLabs for them
    private List<String> requestGroups;
    // filtered list of LIMS dataAccessEmails minus non mskcc & zzPDL, user id list only, @mskcc.org removed
    private List<String> dataAccessEmails;

    private List<String> fastqs; // list of all fastqs for that request
}
