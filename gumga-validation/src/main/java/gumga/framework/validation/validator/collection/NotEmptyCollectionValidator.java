package gumga.framework.validation.validator.collection;

import com.google.common.base.Optional;
import gumga.framework.validation.GumgaValidationError;
import gumga.framework.validation.validator.GumgaAbstractValidator;
import java.util.Collection;
import org.springframework.validation.Errors;

/**
 * Validate is the {@link Collection} is not empty
 * 
 */
public class NotEmptyCollectionValidator extends GumgaAbstractValidator<Collection<?>> {

	public static final String ERROR_CODE = "validation.collection.notNullOrEmpty";

	public NotEmptyCollectionValidator() {
		super(ERROR_CODE);
	}

	@Override
	public Optional<GumgaValidationError> validate(Collection<?> value, Errors errors) {
		return this.doValidation(value != null && !value.isEmpty());
	}

}
