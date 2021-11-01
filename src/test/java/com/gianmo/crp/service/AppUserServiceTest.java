package com.gianmo.crp.service;

import com.gianmo.crp.PersistedItTest;
import com.gianmo.crp.database.entity.AppUser;
import com.gianmo.crp.database.entity.Referral;
import com.gianmo.crp.event.ReferralConsumedEventNotifier;
import com.gianmo.crp.exception.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

class AppUserServiceTest extends PersistedItTest {
	@Autowired
	private AppUserService appUserService;

	@Autowired
	private ReferralService referralService;

	@MockBean
	private ReferralConsumedEventNotifier referralConsumedEventNotifier;

	@Test
	void givenANewUserWithAValidReferralCodeShouldPersistAndPublishAnEvent() {
		final AppUser anExistingRefUser = AppUser.builder()
				.username("a ref user")
				.build();
		appUserService.save(anExistingRefUser);
		final Referral aValidReferralCode = Referral.builder()
				.code("a valid referral code")
				.appUser(anExistingRefUser)
				.build();
		referralService.save(aValidReferralCode);
		final AppUser aNewUser = AppUser.builder()
				.username("a new user")
				.build();
		appUserService.save(aNewUser,
				"a valid referral code");
		verify(referralConsumedEventNotifier).publish(aNewUser, aValidReferralCode);
	}

	@Test
	void givenANewUserWithAInvalidReferralCodeShouldPersistAndPublishAnEvent() {
		final AppUser aNewUser = AppUser.builder()
				.username("another new user")
				.build();
		final Exception exception = assertThrows(EntityNotFoundException.class, () -> appUserService.save(aNewUser,
				"an invalid referral code"));
		assertThat(exception.getMessage())
				.isEqualTo("No [com.gianmo.crp.database.entity.Referral] can be found with code = an invalid referral code");
		verifyNoInteractions(referralConsumedEventNotifier);

	}

}