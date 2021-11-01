package com.gianmo.crp.api.controller;

import com.gianmo.crp.api.dto.ReferralInputDTO;
import com.gianmo.crp.api.dto.ReferralOutputDTO;
import com.gianmo.crp.database.entity.AppUser;
import com.gianmo.crp.database.entity.Referral;
import com.gianmo.crp.service.AppUserService;
import com.gianmo.crp.service.ReferralService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ReferralControllerTest extends ControllerItTest {
	@Autowired
	private ReferralService service;

	@Autowired
	private AppUserService appUserService;

	private Referral anExistingReferral;
	private AppUser anExistingUser;

	@BeforeEach
	void setUp() {
		anExistingUser = AppUser.builder()
				.username("a referral user name")
				.password("a referral user password")
				.build();
		appUserService.save(anExistingUser);

		anExistingReferral = Referral.builder()
				.code("a referral code")
				.consumed(false)
				.appUser(anExistingUser)
				.build();
		service.save(anExistingReferral);
	}

	@AfterEach
	void tearDown() {
		service.deleteAll();
		appUserService.deleteAll();
	}

	@Test
	void whenFindAllIsCalledShouldReturnAllTheExistingReferrals() {
		final ReferralOutputDTO[] actual = this.restTemplate.getForObject("/api/v1/referral", ReferralOutputDTO[].class);
		assertThat(actual).hasSize(1);
	}

	@Test
	void givenANewReferralWhenSaveIsCalledShouldCorrectlyPersistTheNewReferral() {
		final List<Referral> referralsBefore = service.findAll();

		final ReferralOutputDTO actual = this.restTemplate.postForObject("/api/v1/referral",
				ReferralInputDTO.builder()
						.code("another referral code")
						.appUserId(anExistingUser.getId())
						.build(), ReferralOutputDTO.class);

		assertThat(service.findOne(actual.getId())).isNotNull();
		final List<Referral> referralsAfter = service.findAll();
		assertThat(referralsBefore).hasSize(1);
		assertThat(referralsAfter).hasSize(2);
		assertThat(referralsAfter).anyMatch(it -> it.getCode().equals("another referral code"));

	}

	@Test
	void givenAValidReferralIdentifierWhereFindOneIsCalledShouldReturnTheExistingReferral() {
		final ReferralOutputDTO actual = this.restTemplate.getForObject("/api/v1/referral/{id}", ReferralOutputDTO.class,
				anExistingReferral.getId());
		assertThat(actual).isNotNull();
		assertThat(actual.getCode()).isEqualTo("a referral code");
	}

	@Test
	void givenAValidReferralIdentifierWhereFindOneByCodeIsCalledShouldReturnTheExistingUser() {
		final ReferralOutputDTO actual = this.restTemplate.getForObject("/api/v1/referral/by-code/{code}", ReferralOutputDTO.class,
				anExistingReferral.getCode());
		assertThat(actual).isNotNull();
		assertThat(actual.getCode()).isEqualTo("a referral code");
	}

	@Test
	void givenAValidReferralWhenUpdateIsCalledShouldChangeTheSpecificNewFields() {
		final ReferralOutputDTO actual = this.restTemplate.patchForObject("/api/v1/referral/{id}",
				ReferralOutputDTO.builder()
						.code("a referral code updated")
						.build(), ReferralOutputDTO.class, anExistingReferral.getId());

		assertThat(service.findOne(actual.getId())).isNotNull();
		assertThat(actual.getCode()).isEqualTo("a referral code updated");
		assertThat(actual.getAppUserId()).isEqualTo(anExistingUser.getId());
	}

}