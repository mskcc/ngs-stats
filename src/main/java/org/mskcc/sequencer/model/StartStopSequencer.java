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
    private String directoryName;  // "190226_MICHELLE_0094_AHJCGTDMXX"
    @Column(length = 150)
    private String run;   // for example, date removed - "MICHELLE_0094_AHJCGTDMXX"

    @Column(length = 25)
    private String sequencer;

    private Date start;
    private Date stop;

    private Date lastModified;

    // "181030_MOMO_0301_AHN33WBCX2" remove first 7 characters and _ from prefix
    public static String removeDateFromRun(String run) {
        if (run.length() > 10)
            return run.substring(7);
        return null;
    }
}