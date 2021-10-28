package com.gianmo.crp.api.validation;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.List;

@Value
@AllArgsConstructor
public class ValidationErrorResponse {
	List<Violation> violations;

	@Value
	@AllArgsConstructor
	public static class Violation {
		String fieldName;
		String message;
	}
}
