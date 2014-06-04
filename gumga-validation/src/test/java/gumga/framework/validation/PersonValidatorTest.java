package gumga.framework.validation;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

public class PersonValidatorTest {

	@Test
	public void test() {
		Person person = new Person();
		person.setName("José");

		Errors errors = validate(person);

		assertNotNull(errors.getFieldError("name"));

	}

	@Test
	public void should_be_tested() {
		Person person = new Person();
		Errors errors = validate(person);
		assertTrue(errors.hasErrors());
	}

	private Errors validate(Person person) {
		Errors errors = new BeanPropertyBindingResult(person, "person");
		new PersonValidator().validate(person, errors);
		return errors;
	}

}
