package org.mskcc;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StatsApplicationTests {

	@Test @Ignore // Firewall turned on 4/5, db connection change to https?
	public void contextLoads() {
	}
}