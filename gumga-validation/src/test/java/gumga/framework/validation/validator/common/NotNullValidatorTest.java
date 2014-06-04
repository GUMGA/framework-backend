package gumga.framework.validation.validator.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import gumga.framework.validation.GumgaValidationError;

import org.junit.Test;
import org.springframework.validation.Errors;

import com.google.common.base.Optional;

public class NotNullValidatorTest {

	private static final String ERROR_CODE = "error code";
	private static final Errors errors = mock(Errors.class);

	@Test
	public void should_be_invalid_if_value_is_null() {

		Optional<GumgaValidationError> result = new NotNullValidator(ERROR_CODE).validate(null, errors);

		assertTrue(result.isPresent());
		assertEquals(ERROR_CODE, result.get().getCode());
		assertNotNull(result.get().getArgs());
		assertEquals(0, result.get().getArgs().length);
	}

	@Test
	public void should_be_valid_if_value_is_not_null() {
		Optional<GumgaValidationError> result = new NotNullValidator(ERROR_CODE).validate("not null value", errors);
		assertFalse(result.isPresent());

	}
}
