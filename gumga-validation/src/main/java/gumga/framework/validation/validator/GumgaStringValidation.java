package gumga.framework.validation.validator;

import gumga.framework.validation.GumgaFieldValidator;
import gumga.framework.validation.validator.string.IllegalWordsValidator;
import gumga.framework.validation.validator.string.NotNullOrEmptyValidator;

public class GumgaStringValidation {

	private GumgaStringValidation() { }

	public static final GumgaFieldValidator<String> containsIllegalWords(String... illegalWords) {
		return new IllegalWordsValidator(illegalWords);
	}

	public static final GumgaFieldValidator<String> notNullOrEmpty() {
		return new NotNullOrEmptyValidator();
	}

}
