package org.mskcc.sequencer.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StartStopSequencer {
    @Id
    @Column(length = 150)
    private String run;
    @Column(length = 25)
    private String sequencer;

    private Date start;
    private Date stop;

    private Date lastModified;
}