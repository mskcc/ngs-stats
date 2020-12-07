package org.mskcc.permissions.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@AllArgsConstructor
@EqualsAndHashCode
public class LabMemberId implements Serializable {
    private String pi;
    private String member;
}
