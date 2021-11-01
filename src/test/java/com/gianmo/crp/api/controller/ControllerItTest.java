package com.gianmo.crp.api.controller;

import com.gianmo.crp.CustomerReferralProgramAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = CustomerReferralProgramAPI.class)
public abstract class ControllerItTest {

	@Autowired
	protected TestRestTemplate restTemplate;

}
