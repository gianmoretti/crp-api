package com.gianmo.crp.service;

import com.gianmo.crp.database.entity.AppUser;
import com.gianmo.crp.database.entity.Referral;
import com.gianmo.crp.database.repository.AppUserRepo;
import com.gianmo.crp.event.ReferralConsumedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventsListenerTest {

	@InjectMocks
	private EventsListener eventsListener;

	@Mock
	private AppUserRepo appUserRepo;

	@Mock
	private ReferralService referralService;
	
	private AppUser anExistingRefUser;
	private AppUser aNewUser;

	@BeforeEach
	void setUp() {

		ReflectionTestUtils.setField(eventsListener, "referralProgramNumberThreshold", 5);
		ReflectionTestUtils.setField(eventsListener, "referralProgramCreditBonusReferenceUser", 9);
		ReflectionTestUtils.setField(eventsListener, "referralProgramCreditBonusSignedUser", 11);

		anExistingRefUser = AppUser.builder()
				.username("a ref user")
				.build();
		aNewUser = AppUser.builder()
				.username("a new user")
				.build();
	}

	@Test
	void givenAValidReferralUsedOnlyOnceWhenEventIsProcessedShouldComputeCorrectlyCredits() {
		when(appUserRepo.countByMyReferral(any(Referral.class))).thenReturn(1L);
		final Referral aValidReferralCode = Referral.builder()
				.code("a valid referral code")
				.appUser(anExistingRefUser)
				.consumed(false)
				.build();

		eventsListener.handleReferralConsumedEvent(
				ReferralConsumedEvent.builder()
						.appUser(aNewUser)
						.referral(aValidReferralCode)
						.build());

		assertThat(aNewUser.getCredit()).isEqualTo(11L);
		assertThat(anExistingRefUser.getCredit()).isNull();
	}

	@Test
	void givenAValidReferralUsedMoreTimesWhenEventIsProcessedShouldComputeCorrectlyCredits() {
		when(appUserRepo.countByMyReferral(any(Referral.class))).thenReturn(10L);
		final Referral aValidReferralCode = Referral.builder()
				.code("a valid referral code")
				.appUser(anExistingRefUser)
				.consumed(false)
				.build();

		eventsListener.handleReferralConsumedEvent(
				ReferralConsumedEvent.builder()
						.appUser(aNewUser)
						.referral(aValidReferralCode)
						.build());

		assertThat(aNewUser.getCredit()).isEqualTo(11L);
		assertThat(anExistingRefUser.getCredit()).isEqualTo(9L);
	}

	@Test
	void givenAnAlreadyConsumedReferralWhenEventIsProcessedShouldNotChangeCredits() {
		final Referral aValidReferralCode = Referral.builder()
				.code("a valid referral code")
				.appUser(anExistingRefUser)
				.consumed(true)
				.build();

		eventsListener.handleReferralConsumedEvent(
				ReferralConsumedEvent.builder()
						.appUser(aNewUser)
						.referral(aValidReferralCode)
						.build());

		assertThat(aNewUser.getCredit()).isNull();
		assertThat(anExistingRefUser.getCredit()).isNull();
	}
}