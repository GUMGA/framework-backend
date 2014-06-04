package gumga.framework.validation.validator.common;

import gumga.framework.validation.GumgaFieldValidator;
import gumga.framework.validation.GumgaValidationError;

import org.springframework.validation.Errors;

import com.google.common.base.Optional;

/**
 * Validate if the field has error
 */
public class FieldWithoutErrorsValidator implements GumgaFieldValidator<Object> {

	public static final String ERROR_CODE = "validation.fieldWithoutErrors";

	private String field;
	private String code;

	public FieldWithoutErrorsValidator(String field) {
		this(field, ERROR_CODE);
	}

	public FieldWithoutErrorsValidator(String field, String code) {
		this.field = field;
		this.code = code;
	}

	@Override
	public Optional<GumgaValidationError> validate(Object value, Errors errors) {
		if (errors.hasFieldErrors(this.field))
			return Optional.of(new GumgaValidationError(code, new Object[] { this.field }));

		return Optional.<GumgaValidationError> absent();
	}
}
