package com.gianmo.crp.api.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(builderMethodName = "referralOutputDTOBuilder")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ReferralOutputDTO extends ReferralInputDTO {
	private Integer id;
}
