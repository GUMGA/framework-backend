package gumga.framework.validation;

import static gumga.framework.validation.validator.GumgaCommonValidator.notNull;
import static gumga.framework.validation.validator.GumgaStringValidation.notNullOrEmpty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import gumga.framework.validation.exception.InvalidEntityException;
import gumga.framework.validation.validator.common.IsFalseValidator;
import gumga.framework.validation.validator.common.IsTrueValidator;
import gumga.framework.validation.validator.common.NotNullValidator;
import gumga.framework.validation.validator.string.NotNullOrEmptyValidator;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

public class GumgaValidatorTest {

	private static final String NAME_FIELD = "name";
	private static final String BIRTH_DATE_FIELD = "birthDate";
	private Errors errors = Mockito.mock(Errors.class);

	@Before
	public void setUp() {
		Person person = Mockito.mock(Person.class);
		this.errors = new BeanPropertyBindingResult(person, "person");
	}

	@Test
	public void should_be_valid_if_all_validations_is_true() {

		GumgaValidator.with(errors) //
				.check(BIRTH_DATE_FIELD, new Date(), notNull()) //
				.check(NAME_FIELD, "any name", notNullOrEmpty());

		assertFalse(errors.hasErrors());
		assertFalse(errors.hasFieldErrors(NAME_FIELD));
		assertFalse(errors.hasFieldErrors(BIRTH_DATE_FIELD));

	}

	@Test
	public void should_be_invalid_if_any_validation_is_false() {

		GumgaValidator.with(errors) //
				.check(BIRTH_DATE_FIELD, new Date(), notNull()) //
				.check(NAME_FIELD, "", notNullOrEmpty());

		assertTrue(errors.hasErrors());
		assertTrue(errors.hasFieldErrors(NAME_FIELD));
		assertEquals(NotNullOrEmptyValidator.ERROR_CODE, errors.getFieldError(NAME_FIELD).getCode());
		assertFalse(errors.hasFieldErrors(BIRTH_DATE_FIELD));

	}

	@Test
	public void should_be_invalid_if_expression_is_true() {

		GumgaValidator.with(errors) //
				.checkIsFalse(BIRTH_DATE_FIELD, true);

		assertTrue(errors.hasErrors());
		assertTrue(errors.hasFieldErrors(BIRTH_DATE_FIELD));
		assertEquals(IsFalseValidator.ERROR_CODE, errors.getFieldError(BIRTH_DATE_FIELD).getCode());

	}

	@Test
	public void should_be_valid_if_expression_is_false() {

		GumgaValidator.with(errors) //
				.checkIsFalse(BIRTH_DATE_FIELD, false);

		assertFalse(errors.hasErrors());
		assertFalse(errors.hasFieldErrors(BIRTH_DATE_FIELD));

	}

	@Test
	public void should_be_invalid_if_expression_is_false() {

		GumgaValidator.with(errors) //
				.checkIsTrue(BIRTH_DATE_FIELD, false);

		assertTrue(errors.hasErrors());
		assertTrue(errors.hasFieldErrors(BIRTH_DATE_FIELD));
		assertEquals(IsTrueValidator.ERROR_CODE, errors.getFieldError(BIRTH_DATE_FIELD).getCode());

	}

	@Test
	public void should_be_valid_if_expression_is_true() {

		GumgaValidator.with(errors) //
				.checkIsTrue(BIRTH_DATE_FIELD, true);

		assertFalse(errors.hasErrors());
		assertFalse(errors.hasFieldErrors(BIRTH_DATE_FIELD));

	}

	@Test
	public void should_be_invalid_if_expression_is_null() {

		GumgaValidator.with(errors) //
				.checkNotNull(BIRTH_DATE_FIELD, null);

		assertTrue(errors.hasErrors());
		assertTrue(errors.hasFieldErrors(BIRTH_DATE_FIELD));
		assertEquals(NotNullValidator.ERROR_CODE, errors.getFieldError(BIRTH_DATE_FIELD).getCode());

	}

	@Test
	public void should_be_valid_if_expression_is_not_null() {

		GumgaValidator.with(errors) //
				.checkNotNull(BIRTH_DATE_FIELD, new Date());

		assertFalse(errors.hasErrors());
		assertFalse(errors.hasFieldErrors(BIRTH_DATE_FIELD));

	}

	@Test(expected = InvalidEntityException.class)
	public void should_throw_exception_if_validation_is_false() {

		GumgaValidator.with(errors) //
				.check(BIRTH_DATE_FIELD, new Date(), notNull()) //
				.check(NAME_FIELD, "", notNullOrEmpty()).check();

	}

	@Test(expected = InvalidEntityException.class)
	public void should_throw_exception_if_validation_is_false_and_have_no_errors() {

		GumgaValidator.get() //
				.checkNotNull(BIRTH_DATE_FIELD, new Date()) //
				.check(NAME_FIELD, "", notNullOrEmpty()).check();

	}

}
