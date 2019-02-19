package org.mskcc.picardstats.repository;

import org.mskcc.picardstats.model.PicardFile;
import org.springframework.data.repository.CrudRepository;

public interface PicardFileRepository extends CrudRepository<PicardFile, String> {
}
