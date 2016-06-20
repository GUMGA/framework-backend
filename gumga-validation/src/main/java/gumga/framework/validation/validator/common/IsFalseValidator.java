package gumga.framework.validation.validator.common;

import com.google.common.base.Optional;
import gumga.framework.validation.GumgaValidationError;
import gumga.framework.validation.validator.GumgaAbstractValidator;
import org.springframework.validation.Errors;

/**
 * Validate if the specified value is False
 */
public class IsFalseValidator extends GumgaAbstractValidator<Boolean> {

	public static final String ERROR_CODE = "validation.isFalse";
	public static final IsFalseValidator INSTANCE = new IsFalseValidator(ERROR_CODE);

	public IsFalseValidator(String code) {
		super(code);
	}

	@Override
	public Optional<GumgaValidationError> validate(Boolean value, Errors errors) {
		return doValidation(!value);
	}

}
