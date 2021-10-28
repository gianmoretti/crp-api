package com.gianmo.crp.api.controller;

import com.gianmo.crp.api.dto.ReferralInputDTO;
import com.gianmo.crp.api.dto.ReferralOutputDTO;
import com.gianmo.crp.database.entity.Referral;
import com.gianmo.crp.service.ReferralService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/referral")
public class ReferralController {

	private final ReferralService service;
	private final ModelMapper mapper;

	private Function<Referral, ReferralOutputDTO> toReferralDto;
	private Function<ReferralInputDTO, Referral> toReferral;

	@PostConstruct
	public void init() {
		toReferralDto = referral -> mapper.map(referral, ReferralOutputDTO.class);
		toReferral = referralDto -> mapper.map(referralDto, Referral.class);
	}

	/**
	 * Save a new referral
	 *
	 * @param dto referral dto
	 * @return
	 */
	@PostMapping
	public ResponseEntity<ReferralOutputDTO> save(@RequestBody final ReferralInputDTO dto) {
		final Referral referral = Optional.of(dto).map(toReferral).get();
		return ResponseEntity.ok(Optional.of(service.save(referral, dto.getAppUserId())).map(toReferralDto).get());
	}

	/**
	 * Find a referral by id
	 *
	 * @param id
	 * @return referral program related to the input id
	 */
	@GetMapping("/{id}")
	public ResponseEntity<ReferralOutputDTO> findOne(@PathVariable("id") final int id) {
		return ResponseEntity.ok(Optional.of(service.findOne(id)).map(toReferralDto).get());
	}

	/**
	 * Find a referral by code
	 *
	 * @param code unique code
	 * @return referral program related to the input code
	 */
	@GetMapping("/by-code/{code}")
	public ResponseEntity<ReferralOutputDTO> findOneByCode(@PathVariable("code") final String code) {
		return ResponseEntity.ok(Optional.of(service.findOneByCode(code)).map(toReferralDto).get());
	}

	/**
	 * Find all referrals
	 *
	 * @return the list of existing referrals
	 */
	@GetMapping
	public ResponseEntity<Collection<ReferralOutputDTO>> findAll() {
		return ResponseEntity
				.ok(service.findAll().stream().map(toReferralDto).collect(Collectors.toSet()));
	}

	/**
	 * Update referral
	 *
	 * @param id  referral id to update
	 * @param dto fields updated
	 * @return
	 */
	@PatchMapping("/{id}")
	public ResponseEntity<ReferralOutputDTO> update(@PathVariable("id") final int id, @RequestBody final ReferralInputDTO dto) {
		final Referral referral = Optional.of(dto).map(toReferral).get();
		referral.setId(id);
		return ResponseEntity.ok(Optional.of(service.update(referral)).map(toReferralDto).get());
	}

	/**
	 * Delete referral by id
	 *
	 * @param id
	 * @return
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") final int id) {
		service.deleteById(id);
		return ResponseEntity.ok().build();
	}

}