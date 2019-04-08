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
/*
Maintains timestamps for demux complete, start archiving (FINISHED file) and archiving complete(fastq.md5.archive)
<BR>
If a run is re-dumuxed the directory name has an added suffix such as "_A1".
 */
public class StartStopArchiveFastq {
    @Id @Column(length = 150)
    private String directoryName;
    @Column(length = 150)
    private String run;

    @Column(length = 25)
    private String sequencer;

    private Date demuxStop;
    private Date archiveStart;
    private Date archiveStop;

    private Date lastModified;

    public static String getSequencerName(String run) {
        String name = run.substring(0, run.indexOf("_"));

        return name.toLowerCase();
    }

    public static String getRun(String directoryName) {
        String [] parts = directoryName.split("_");
        if (parts.length >= 4) {
            return parts[0] + "_" + parts[1] + "_" + parts[2];
        } else {
            return directoryName;
        }
    }
}