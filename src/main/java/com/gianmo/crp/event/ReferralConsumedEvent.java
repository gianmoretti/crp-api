package com.gianmo.crp.event;

import com.gianmo.crp.database.entity.Referral;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ReferralConsumedEvent {
	private final Referral referral;
	private final LocalDateTime when;
}
