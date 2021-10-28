package com.gianmo.crp.conf;

import com.gianmo.crp.api.validation.ValidationErrorResponse;
import com.gianmo.crp.api.validation.ValidationErrorResponse.Violation;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@ControllerAdvice
public class ControllerValidationExceptionHandler {

	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	ValidationErrorResponse onConstraintValidationException(final ConstraintViolationException e) {

		log.debug("Error during validation", e);
		log.info("Error during validation [{}]", e.getConstraintViolations());
		final List<Violation> errors = e.getConstraintViolations().stream()
				.map(cv -> new Violation(cv.getPropertyPath().toString(), cv.getMessage()))
				.collect(Collectors.toList());

		return new ValidationErrorResponse(errors);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	ValidationErrorResponse onMethodArgumentNotValidException(final MethodArgumentNotValidException e) {

		log.debug("Error during validation", e);
		log.warn("Error during validation: {}", e.getBindingResult());
		final List<Violation> errors = e.getBindingResult().getFieldErrors().stream()
				.map(fieldError -> new Violation(fieldError.getField(), fieldError.getDefaultMessage()))
				.collect(Collectors.toList());

		return new ValidationErrorResponse(errors);
	}

}
