package com.gianmo.crp.service;

import com.gianmo.crp.database.entity.AppUser;
import com.gianmo.crp.database.entity.Referral;
import com.gianmo.crp.database.repository.AppUserRepo;
import com.gianmo.crp.database.repository.ReferralRepo;
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
public class ReferralService {

	private final ReferralRepo repository;
	private final AppUserRepo userRepo;
	private final ModelMapper notNullMapper;

	@PostConstruct
	private void init() {
		notNullMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
	}

	public Referral save(final Referral referral) {
		notNull(referral, "The passed object should not be null.");
		return repository.save(referral);
	}

	public Referral save(final Referral referral, final Integer userId) {
		notNull(referral, "The passed object should not be null.");
		notNull(userId, "The passed user id should not be null.");
		if (!userRepo.existsById(userId)) {
			throw new EntityNotFoundException(AppUser.class, userId);
		}
		return repository.save(referral);
	}

	public Referral update(final Referral referral) {
		notNull(referral, "The passed object should not be null.");
		notNull(referral.getId(), "The passed id should not be null.");
		final Referral existing = findOne(referral.getId());
		notNullMapper.map(referral, existing);
		return repository.save(existing);
	}

	public Referral findOne(final int id) {
		return repository.findById(id).orElseThrow(EntityNotFoundException.supplier(Referral.class, id));
	}

	public Referral findOneByCode(final String code) {
		return repository.findOneByCode(code).orElseThrow(EntityNotFoundException.supplier(Referral.class, "code", code));
	}

	public List<Referral> findAll() {
		return repository.findAll();
	}

	public void deleteById(final Integer id) {
		repository.deleteById(id);
	}

	public void deleteAll() {
		repository.deleteAll();
	}
}
