package com.gianmo.crp.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Data
@Builder(builderMethodName = "userOutputDTOBuilder")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AppUserOutputDTO extends AppUserInputDTO {
	private Integer id;
	private Integer credit;
	private String myReferralCode;
}
