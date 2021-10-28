package com.gianmo.crp.api.controller;

import com.gianmo.crp.api.dto.AppUserInputDTO;
import com.gianmo.crp.api.dto.AppUserOutputDTO;
import com.gianmo.crp.database.entity.AppUser;
import com.gianmo.crp.service.AppUserService;
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
@RequestMapping("/api/v1/user")
public class UserController {

	private final AppUserService service;
	private final ModelMapper mapper;

	private Function<AppUser, AppUserOutputDTO> toUserDto;
	private Function<AppUserInputDTO, AppUser> toUser;

	@PostConstruct
	public void init() {
		toUserDto = user -> mapper.map(user, AppUserOutputDTO.class);
		toUser = userDto -> mapper.map(userDto, AppUser.class);
	}

	@PostMapping
	public ResponseEntity<AppUserOutputDTO> save(@RequestBody final AppUserInputDTO dto) {
		final AppUser user = Optional.of(dto).map(toUser).get();
		final String myReferralCode = dto.getMyReferralCode();
		return ResponseEntity.ok(
				Optional.of(myReferralCode != null
						? service.save(user, myReferralCode)
						: service.save(user)
				).map(toUserDto).get());
	}

	@GetMapping("/{id}")
	public ResponseEntity<AppUserOutputDTO> findOne(@PathVariable("id") final int id) {
		return ResponseEntity.ok(Optional.of(service.findOne(id)).map(toUserDto).get());
	}

	@GetMapping
	public ResponseEntity<Collection<AppUserOutputDTO>> findAll() {
		return ResponseEntity
				.ok(service.findAll().stream().map(toUserDto).collect(Collectors.toSet()));
	}

	@PatchMapping("/{id}")
	public ResponseEntity<AppUserOutputDTO> update(@PathVariable("id") final int id, @RequestBody final AppUserInputDTO dto) {
		final AppUser user = Optional.of(dto).map(toUser).get();
		user.setId(id);
		return ResponseEntity.ok(Optional.of(service.update(user)).map(toUserDto).get());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") final int id) {
		service.deleteById(id);
		return ResponseEntity.ok().build();
	}

}