package org.mskcc.permissions.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class RequestPermissions {
    private String labName;
    private List<String> labMembers;
    private List<String> requestGroups; // UNIX groups that have read access such as 'cmoigo', 'bicigo', 'isabl'
    private List<String> fastqs; // list of all fastqs for that request
}
