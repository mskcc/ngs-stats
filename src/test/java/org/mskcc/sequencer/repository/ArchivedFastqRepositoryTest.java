package org.mskcc.sequencer.repository;

import org.junit.jupiter.api.Test;
import org.mskcc.sequencer.model.ArchivedFastq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.net.URI;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
class ArchivedFastqRepositoryTest {

    @Autowired
    private ArchivedFastqRepository archivedFastqRepository;

    @Test
    void createFastq() {
        ArchivedFastq f = new ArchivedFastq("MICHELLE_001", "MICHELLE_001", "08822", "08822_1", "08822_1.fastq.gz", new Date(), 1L, new Date());
        ArchivedFastq saved = archivedFastqRepository.save(f);
        assertNotNull(saved);
        System.out.println(saved);
    }

    @Test
    void findRecentlyArchived() {
        Date timestampOlder = new Date(System.currentTimeMillis() - (100 * 60000));
        ArchivedFastq f1 = new ArchivedFastq("MICHELLE_002", "MICHELLE_002", "08822", "08822_2", "08822_2.fastq.gz", timestampOlder, 1L, timestampOlder);
        archivedFastqRepository.save(f1);
        ArchivedFastq f2 = new ArchivedFastq("MICHELLE_001", "MICHELLE_001", "08822", "08822_1", "08822_1.fastq.gz", new Date(), 1L, new Date());
        archivedFastqRepository.save(f2);

        long startTime = System.currentTimeMillis() - (50*60000);
        List<String> result = archivedFastqRepository.findRecentlyArchived(new Date(startTime));
        assertEquals(1, result.size());
    }
}