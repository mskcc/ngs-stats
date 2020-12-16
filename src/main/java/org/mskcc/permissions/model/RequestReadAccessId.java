package org.mskcc.permissions.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class RequestReadAccessId implements Serializable {
    private String request;
    private String member;
}
