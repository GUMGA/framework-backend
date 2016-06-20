package gumga.framework.validation.validator.common;

import com.google.common.base.Optional;
import gumga.framework.validation.GumgaValidationError;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import org.springframework.validation.Errors;

public class IsTrueValidatorTest {

	private static final String ERROR_CODE = "error code";
	private static final Errors errors = mock(Errors.class);

	@Test
	public void should_be_invalid_if_value_is_false() {
		Optional<GumgaValidationError> result = new IsFalseValidator(ERROR_CODE).validate(Boolean.TRUE, errors);

		assertTrue(result.isPresent());
		assertEquals(ERROR_CODE, result.get().getCode());
		assertNotNull(result.get().getArgs());
		assertEquals(0, result.get().getArgs().length);
	}

	@Test
	public void should_be_valid_if_value_is_true() {
		Optional<GumgaValidationError> result = new IsFalseValidator(ERROR_CODE).validate(Boolean.FALSE, errors);
		assertFalse(result.isPresent());

	}
}
