package se.gustavkarlsson.officemap.test;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

public class AssertValidation extends AbstractAssert<AssertValidation, Object> {

	private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

	public AssertValidation(final Object actual) {
		super(actual, AssertValidation.class);
	}

	public static AssertValidation assertThat(final Object actual) {
		return new AssertValidation(actual);
	}

	public AssertValidation isValid() {
		isNotNull();

		final Set<ConstraintViolation<Object>> constraintViolations = validator.validate(actual);

		if (constraintViolations.isEmpty()) {
			return this;
		}
		final ConstraintViolation<Object> violation = constraintViolations.iterator().next();
		throw new AssertionError(violation.getPropertyPath() + " " + violation.getMessage());
	}

	public AssertValidation isInvalid() {
		isNotNull();

		final Set<ConstraintViolation<Object>> constraintViolations = validator.validate(actual);

		if (constraintViolations.isEmpty()) {
			throw new AssertionError("object is valid");
		}
		return this;
	}

	public AssertValidation hasInvalid(final String propertyPath) {
		final String errorMessage = "expected " + propertyPath + " to be invalid, but it was valid";

		final Set<ConstraintViolation<Object>> constraintViolations = validator.validate(actual);

		Assertions.assertThat(constraintViolations).overridingErrorMessage(errorMessage).isNotEmpty();
		for (final ConstraintViolation<Object> violation : constraintViolations) {
			if (violation.getPropertyPath().toString().equals(propertyPath)) {
				return this;
			}
		}
		throw new AssertionError(errorMessage);
	}

}
