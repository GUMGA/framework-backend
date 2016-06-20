package gumga.framework.validation.validator.common;

import com.google.common.base.Optional;
import gumga.framework.validation.GumgaFieldValidator;
import gumga.framework.validation.GumgaValidationError;
import gumga.framework.validation.validator.GumgaAbstractValidator;
import org.springframework.validation.Errors;

/**
 * Validate if the specified value is True
 */
public class IsTrueValidator extends GumgaAbstractValidator<Boolean> {

	public static final String ERROR_CODE = "validation.isTrue";
	public static final GumgaFieldValidator<Boolean> INSTANCE = new IsTrueValidator(ERROR_CODE);

	public IsTrueValidator(String code) {
		super(code);
	}

	@Override
	public Optional<GumgaValidationError> validate(Boolean value, Errors errors) {
		return doValidation(value);
	}

}
