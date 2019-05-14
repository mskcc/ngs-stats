package org.mskcc.picardstats.model;

import javax.persistence.*;

@Entity
public class OxoMetrics {
    @Id
    @Column(length = 150)
    public String filename;
    @Column(length = 32)
    public String md5RRS;
    public Double G_REF_OXO_Q;
    @OneToOne
    @JoinColumn(name = "filename")
    private PicardFile picardFile;
}
