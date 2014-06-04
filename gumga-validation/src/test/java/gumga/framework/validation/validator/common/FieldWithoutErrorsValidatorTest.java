package gumga.framework.validation.validator.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import gumga.framework.validation.GumgaValidationError;

import org.junit.Test;
import org.springframework.validation.Errors;

import com.google.common.base.Optional;

public class FieldWithoutErrorsValidatorTest {

	private static final String FIELD = "name";
	private static final String VALIDATION_ERROR = "validation.error";

	@Test
	public void should_be_invalid_if_the_specified_field_has_error() {

		Errors errors = mock(Errors.class);
		when(errors.hasFieldErrors(FIELD)).thenReturn(Boolean.TRUE);

		FieldWithoutErrorsValidator validator = new FieldWithoutErrorsValidator(FIELD, VALIDATION_ERROR);
		Optional<GumgaValidationError> result = validator.validate("any", errors);

		assertTrue(result.isPresent());
		assertEquals(VALIDATION_ERROR, result.get().getCode());
		assertNotNull(result.get().getArgs());
		assertEquals(1, result.get().getArgs().length);
		assertEquals(FIELD, result.get().getArgs()[0]);

	}

	@Test
	public void should_be_valid_if_the_specified_field_has_no_error() {

		Errors errors = mock(Errors.class);
		when(errors.hasFieldErrors(FIELD)).thenReturn(Boolean.FALSE);

		FieldWithoutErrorsValidator validator = new FieldWithoutErrorsValidator(FIELD, VALIDATION_ERROR);
		Optional<GumgaValidationError> result = validator.validate("any", errors);

		assertFalse(result.isPresent());

	}

}
