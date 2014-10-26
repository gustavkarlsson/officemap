package se.gustavkarlsson.officemap.test;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.fest.assertions.Assertions;
import org.fest.assertions.GenericAssert;

public class AssertValidation extends GenericAssert<AssertValidation, Object> {

	private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
	
	public AssertValidation(final Object actual) {
		super(AssertValidation.class, actual);
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

		failIfCustomMessageIsSet();
		final ConstraintViolation<Object> violation = constraintViolations.iterator().next();
		throw failure(violation.getPropertyPath() + " " + violation.getMessage());
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
		failIfCustomMessageIsSet();
		throw failure(errorMessage);
	}

}
