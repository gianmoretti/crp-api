package com.gianmo.crp.api.controller;

import com.gianmo.crp.api.dto.AppUserInputDTO;
import com.gianmo.crp.api.dto.AppUserOutputDTO;
import com.gianmo.crp.database.entity.AppUser;
import com.gianmo.crp.service.AppUserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UserControllerTest extends ControllerItTest {
	@Autowired
	private AppUserService service;

	private AppUser anExistingUser;

	@BeforeEach
	void setUp() {
		anExistingUser = AppUser.builder()
				.username("a username")
				.password("a password")
				.build();
		service.save(anExistingUser);
	}

	@AfterEach
	void tearDown() {
		service.deleteAll();
	}

	@Test
	void whenFindAllIsCalledShouldReturnAllTheExistingUsers() {
		final AppUserOutputDTO[] actual = this.restTemplate.getForObject("/api/v1/user", AppUserOutputDTO[].class);
		assertThat(actual).hasSize(1);
	}

	@Test
	void givenANewUserWhenSaveIsCalledShouldCorrectlyPersistTheNewUser() {
		final List<AppUser> appUsersBefore = service.findAll();

		final AppUserOutputDTO actual = this.restTemplate.postForObject("/api/v1/user",
				AppUserInputDTO.builder()
						.username("another user")
						.password("another password")
						.build(), AppUserOutputDTO.class);

		assertThat(service.findOne(actual.getId())).isNotNull();
		final List<AppUser> appUsersAfter = service.findAll();
		assertThat(appUsersBefore).hasSize(1);
		assertThat(appUsersAfter).hasSize(2);
		assertThat(appUsersAfter).anyMatch(it -> it.getUsername().equals("another user"));

	}

	@Test
	void givenAValidUserIdentifierWhereFindOneIsCalledShouldReturnTheExistingUser() {
		final AppUserOutputDTO actual = this.restTemplate.getForObject("/api/v1/user/{id}", AppUserOutputDTO.class, anExistingUser.getId());
		assertThat(actual).isNotNull();
		assertThat(actual.getUsername()).isEqualTo("a username");
	}

	@Test
	void givenAValidUserWhenUpdateIsCalledShouldChangeTheSpecificNewFields() {
		final AppUserOutputDTO actual = this.restTemplate.patchForObject("/api/v1/user/{id}", AppUserOutputDTO.builder()
				.username("a username updated").build(), AppUserOutputDTO.class, anExistingUser.getId());

		assertThat(service.findOne(actual.getId())).isNotNull();
		assertThat(actual.getUsername()).isEqualTo("a username updated");
		assertThat(actual.getPassword()).isEqualTo("a password");
	}

}