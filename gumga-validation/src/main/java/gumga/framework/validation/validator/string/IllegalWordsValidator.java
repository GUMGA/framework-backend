package gumga.framework.validation.validator.string;

import gumga.framework.validation.GumgaFieldValidator;
import gumga.framework.validation.GumgaValidationError;

import org.springframework.validation.Errors;

import com.google.common.base.Optional;

public class IllegalWordsValidator implements GumgaFieldValidator<String> {

	private String[] illegalText;

	public IllegalWordsValidator(String... illegalText) {
		this.illegalText = illegalText;
	}

	@Override
	public Optional<GumgaValidationError> validate(String value, Errors error) {
		if (value == null)
			return Optional.absent();

		String comparableValue = value.toUpperCase();
		for (String illegalText : this.illegalText) {
			if (comparableValue.contains(illegalText.toUpperCase())) {
				return Optional.of(new GumgaValidationError("validation.illegalText", new Object[] { illegalText }));
			}
		}

		return Optional.absent();
	}

}
