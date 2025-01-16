package com.nan_spring_boot.spring_boot_crud;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(locations = "classpath:application_test.properties")
@ActiveProfiles("test")
class SpringBootCrudApplicationTests {

	@Test
	void contextLoads() {
	}

}
