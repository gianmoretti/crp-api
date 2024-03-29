package com.gianmo.crp.service;

import com.gianmo.crp.database.entity.AppUser;
import com.gianmo.crp.database.entity.Referral;
import com.gianmo.crp.database.repository.AppUserRepo;
import com.gianmo.crp.event.ReferralConsumedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

import static org.springframework.util.Assert.notNull;

@Service
@RequiredArgsConstructor
@Transactional
public class EventsListener {

	@Value("${referralProgram.numberThreshold}")
	private Integer referralProgramNumberThreshold;

	@Value("${referralProgram.creditBonus.referenceUser}")
	private Integer referralProgramCreditBonusReferenceUser;

	@Value("${referralProgram.creditBonus.signedUser}")
	private Integer referralProgramCreditBonusSignedUser;

	private final AppUserRepo repository;
	private final ReferralService referralService;

	@EventListener
	void handleReferralConsumedEvent(final ReferralConsumedEvent event) {
		notNull(event, "The passed referral consumed event should not be null.");
		final Referral referral = event.getReferral();
		notNull(referral, "The passed event.referral should not be null.");
		if (!referral.isConsumed()) {
			final AppUser user = event.getAppUser();
			user.setCredit(Optional.ofNullable(user.getCredit()).orElse(0) + referralProgramCreditBonusSignedUser);
			repository.save(user);

			final long numberOfReferrals = repository.countByMyReferral(referral);
			if (numberOfReferrals >= referralProgramNumberThreshold) {
				referral.setConsumed(Boolean.TRUE);
				referralService.save(referral);

				final AppUser refUser = referral.getAppUser();
				refUser.setCredit(Optional.ofNullable(refUser.getCredit()).orElse(0) + referralProgramCreditBonusReferenceUser);
				repository.save(refUser);
			}
		}
	}
}
