package com.gianmo.crp.database.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Data
@Entity
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "appUser")
@Table(name = "REFERRAL")
public class Referral implements AbstractEntity<Integer> {

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "CODE", unique = true, nullable = false)
	private String code;

	@Column(name = "CONSUMED")
	private boolean consumed;

	@ManyToOne
	@ToString.Exclude
	@JsonIgnore
	@JoinColumn(name = "APP_USER_ID")
	private AppUser appUser;

}