package com.gianmo.crp.service;

import com.gianmo.crp.database.entity.AppUser;
import com.gianmo.crp.database.entity.Referral;
import com.gianmo.crp.database.repository.AppUserRepo;
import com.gianmo.crp.database.repository.ReferralRepo;
import com.gianmo.crp.event.ReferralConsumedEventNotifier;
import com.gianmo.crp.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.List;

import static org.springframework.util.Assert.notNull;

@Service
@RequiredArgsConstructor
@Transactional
public class AppUserService {

	private final AppUserRepo repository;
	private final ReferralRepo referralRepository;
	private final ModelMapper notNullMapper;
	private final ReferralConsumedEventNotifier referralConsumedEventNotifier;

	@PostConstruct
	private void init() {
		notNullMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
	}

	public AppUser save(final AppUser user, final String referralCode) {
		notNull(user, "The passed object should not be null.");
		notNull(referralCode, "The passed referral code should not be null.");

		if (!referralRepository.existsByCode(referralCode)) {
			throw new EntityNotFoundException(Referral.class, "code", referralCode);
		}
		final Referral referral = referralRepository.findOneByCode(referralCode).get();
		user.setMyReferral(referral);
		final AppUser appUserSaved = repository.save(user);
		referralConsumedEventNotifier.publish(appUserSaved, referral);
		return appUserSaved;

	}

	public AppUser save(final AppUser user) {
		notNull(user, "The passed object should not be null.");

		return repository.save(user);
	}

	public AppUser update(final AppUser user) {
		notNull(user, "The passed object should not be null.");
		notNull(user.getId(), "The passed id should not be null.");
		final AppUser existing = findOne(user.getId());
		notNullMapper.map(user, existing);
		return repository.save(existing);
	}

	public AppUser findOne(final int id) {
		return repository.findById(id).orElseThrow(EntityNotFoundException.supplier(AppUser.class, id));
	}

	public List<AppUser> findAll() {
		return repository.findAll();
	}

	public void deleteById(final Integer id) {
		repository.deleteById(id);
	}

}
