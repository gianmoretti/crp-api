package com.gianmo.crp.database.repository;

import com.gianmo.crp.database.entity.Referral;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReferralRepo extends JpaRepository<Referral, Integer> {
	Optional<Referral> findOneByCode(String code);

	boolean existsByCode(String code);
}
