package gumga.framework.validation.validator.collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import gumga.framework.validation.GumgaValidationError;

import java.util.ArrayList;
import java.util.HashSet;

import org.junit.Test;
import org.springframework.validation.Errors;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class NotEmptyCollectionValidatorTest {

	private Errors errors = mock(Errors.class);

	@Test
	public void should_be_invalid_if_the_specified_field_is_empty() {


		Optional<GumgaValidationError> result;

		result = new NotEmptyCollectionValidator().validate(new ArrayList<>(), errors);
		assertTrue(result.isPresent());
		assertEquals(NotEmptyCollectionValidator.ERROR_CODE, result.get().getCode());
		
		result = new NotEmptyCollectionValidator().validate(new HashSet<>(), errors);
		assertTrue(result.isPresent());
		assertEquals(NotEmptyCollectionValidator.ERROR_CODE, result.get().getCode());

		result = new NotEmptyCollectionValidator().validate(null, errors);
		assertTrue(result.isPresent());
		assertEquals(NotEmptyCollectionValidator.ERROR_CODE, result.get().getCode());

	}

	@Test
	public void should_be_valid_if_the_specified_field_has_no_error() {

		Optional<GumgaValidationError> result;

		result = new NotEmptyCollectionValidator().validate(Lists.newArrayList("elemento1", "elemento2"), errors);
		assertFalse(result.isPresent());
		
		result = new NotEmptyCollectionValidator().validate(Sets.newHashSet("elemento1", "elemento2"), errors);
		assertFalse(result.isPresent());

	}
	
}
