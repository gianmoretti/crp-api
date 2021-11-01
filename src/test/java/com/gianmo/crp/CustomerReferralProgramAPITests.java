package com.gianmo.crp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomerReferralProgramAPITests {

	@Test
	public void contextLoads() {
	}

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void homeResponse() {
		final String body = this.restTemplate.getForObject("/", String.class);
		assertThat(body).isEqualTo("CRP Api");
	}
}

