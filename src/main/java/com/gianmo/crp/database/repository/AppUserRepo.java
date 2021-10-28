package com.gianmo.crp.database.repository;

import com.gianmo.crp.database.entity.AppUser;
import com.gianmo.crp.database.entity.Referral;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepo extends JpaRepository<AppUser, Integer> {
	long countByMyReferral(Referral referral);
}
