package gumga.framework.validation.validator.common;

import gumga.framework.validation.GumgaValidationError;
import gumga.framework.validation.validator.GumgaAbstractValidator;

import org.springframework.validation.Errors;

import com.google.common.base.Optional;

/**
 * Validate if the specified value is True
 */
public class IsTrueValidator extends GumgaAbstractValidator<Boolean> {

	public static final String ERROR_CODE = "validation.isTrue";

	public IsTrueValidator() {
		super(ERROR_CODE);
	}

	public IsTrueValidator(String code) {
		super(code);
	}

	@Override
	public Optional<GumgaValidationError> validate(Boolean value, Errors errors) {
		return doValidation(value);
	}

}
