package com.gianmo.crp.database.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = { "referrals", "myReferral" })
@Table(name = "APP_USER")
public class AppUser implements AbstractEntity<Integer> {

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "USERNAME", unique = true, nullable = false)
	private String username;

	@Column(name = "PASSWORD")
	private String password;

	@ManyToOne
	@JoinColumn(name = "MY_REFERRAL_ID")
	private Referral myReferral;

	@Column(name = "CREDIT")
	private Integer credit;

	@Builder.Default
	@ToString.Exclude
	@JsonIgnore
	@OneToMany(mappedBy = "appUser", cascade = CascadeType.REMOVE, orphanRemoval = true)
	private final Set<Referral> referrals = new HashSet<>();

}
