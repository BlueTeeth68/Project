package com.mangareader.main;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql("classpath:user.sql")
class ProjectApplicationTests {

	@Test
	void contextLoads() {
	}

}
