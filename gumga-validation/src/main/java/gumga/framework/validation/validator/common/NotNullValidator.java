package gumga.framework.validation.validator.common;

import gumga.framework.validation.GumgaValidationError;
import gumga.framework.validation.validator.GumgaAbstractValidator;

import org.springframework.validation.Errors;

import com.google.common.base.Optional;

/**
 * Validate if the specified value is True
 */
public class NotNullValidator extends GumgaAbstractValidator<Object> {

	public NotNullValidator(String code) {
		super(code);
	}

	@Override
	public Optional<GumgaValidationError> validate(Object value, Errors errors) {
		return doValidation(value != null);
	}

}
