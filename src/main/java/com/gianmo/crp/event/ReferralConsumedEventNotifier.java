package com.gianmo.crp.event;

import com.gianmo.crp.database.entity.AppUser;
import com.gianmo.crp.database.entity.Referral;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Log4j2
@RequiredArgsConstructor
@Service
public class ReferralConsumedEventNotifier {

	private final ApplicationEventPublisher applicationEventPublisher;

	public void publish(final AppUser user, final Referral referral) {
		log.debug("A referral code has been successfully used [user={}, referral={} ", user, referral);
		final ReferralConsumedEvent referralConsumedEvent = ReferralConsumedEvent.builder()
				.referral(referral)
				.when(LocalDateTime.now())
				.build();
		applicationEventPublisher.publishEvent(referralConsumedEvent);
	}

}
