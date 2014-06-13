package gumga.framework.validation.validator;

import gumga.framework.validation.GumgaFieldValidator;
import gumga.framework.validation.validator.common.FieldWithoutErrorsValidator;
import gumga.framework.validation.validator.common.IsFalseValidator;
import gumga.framework.validation.validator.common.IsTrueValidator;
import gumga.framework.validation.validator.common.NotNullValidator;

public class GumgaCommonValidator {

	private GumgaCommonValidator() { }

	public static final GumgaFieldValidator<Boolean> isTrue(String message) {
		return new IsTrueValidator(message);
	}

	public static final GumgaFieldValidator<Boolean> isTrue() {
		return IsTrueValidator.INSTANCE;
	}

	public static final GumgaFieldValidator<Boolean> isFalse(String message) {
		return new IsFalseValidator(message);
	}

	public static final GumgaFieldValidator<Boolean> isFalse() {
		return IsFalseValidator.INSTANCE;
	}

	public static final GumgaFieldValidator<Object> notNull(String message) {
		return new NotNullValidator(message);
	}

	public static final GumgaFieldValidator<Object> notNull() {
		return NotNullValidator.INSTANCE;
	}

	public static final GumgaFieldValidator<Object> fieldWithoutErrors(String field, String message) {
		return new FieldWithoutErrorsValidator(field, message);
	}

	public static final GumgaFieldValidator<Object> fieldWithoutErrors(String field) {
		return new FieldWithoutErrorsValidator(field, "validation.fieldWithoutErrors");
	}

}
