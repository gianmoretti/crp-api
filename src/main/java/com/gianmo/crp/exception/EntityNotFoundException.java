package com.gianmo.crp.exception;

import com.gianmo.crp.database.entity.AbstractEntity;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serializable;
import java.util.Optional;
import java.util.function.Supplier;

@EqualsAndHashCode(callSuper = false)
@ResponseStatus(HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends javax.persistence.EntityNotFoundException {

	private static final long serialVersionUID = -8809033810475844631L;

	public static <E extends AbstractEntity<ID>, ID extends Serializable> Supplier<EntityNotFoundException> supplier(
			final Class<E> clazz, final ID id) {
		return () -> new EntityNotFoundException(clazz, id);
	}

	public static <E extends AbstractEntity<ID>, ID extends Serializable> Supplier<EntityNotFoundException> supplier(
			final Class<E> clazz, final String pkFieldName, final String pkFieldValue) {
		return () -> new EntityNotFoundException(clazz, pkFieldName, pkFieldValue);
	}

	public <E extends AbstractEntity<ID>, ID extends Serializable> EntityNotFoundException(
			final Class<E> clazz,
			final ID id) {
		super(String.format("No [%s] can be found with id: %s",
				Optional.ofNullable(clazz).map(Class::getName).orElse("class not specified"), id));
	}

	public <E extends AbstractEntity<ID>, ID extends Serializable> EntityNotFoundException(
			final Class<E> clazz,
			final String pkFieldName,
			final String pkFieldValue) {

		super(String.format("No [%s] can be found with %s = %s",
				Optional.ofNullable(clazz).map(Class::getName).orElse("class not specified"), pkFieldName, pkFieldValue));
	}
}