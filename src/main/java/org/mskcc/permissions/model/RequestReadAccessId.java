package org.mskcc.permissions.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@AllArgsConstructor
@EqualsAndHashCode
public class RequestReadAccessId implements Serializable {
    private String request;
    private String member;
}
