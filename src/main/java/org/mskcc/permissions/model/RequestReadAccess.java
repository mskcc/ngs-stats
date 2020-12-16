package org.mskcc.permissions.model;

import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Getter @Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@IdClass(RequestReadAccessId.class)
public class RequestReadAccess {
    private static Logger log = LoggerFactory.getLogger(RequestReadAccess.class);

    @Id
    private String request;
    @Id
    private String member;

    boolean isGroup = false; // groups must be specified because the ACL requires ":g:" to work
}