package com.gianmo.crp;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@SpringBootApplication
@RestController
public class CustomerReferralProgramAPI {

	@GetMapping("/")
	String home() {
		return "CRP Api";
	}

	public static void main(final String[] args) {
		log.debug("Starting...");
		SpringApplication.run(CustomerReferralProgramAPI.class, args);
		log.debug("Started...");
	}

}
