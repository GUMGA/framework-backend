package gumga.framework.validation;

import static gumga.framework.validation.validator.GumgaCommonValidator.fieldWithoutErrors;
import static gumga.framework.validation.validator.GumgaCommonValidator.isTrue;
import static gumga.framework.validation.validator.GumgaCommonValidator.notNull;
import static gumga.framework.validation.validator.GumgaStringValidation.containsIllegalWords;
import static gumga.framework.validation.validator.GumgaStringValidation.notNullOrEmpty;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class PersonValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Person.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object obj, Errors errors) {

		Person person = Person.class.cast(obj);

		GumgaValidator.with(errors) //
				
				.check("name", person.getName(), notNull()) //
				.check("name", person.getName(), notNullOrEmpty()) //
				.check("name", person.getName(), containsIllegalWords("Everton", "Tavares")) //
				
				.check("name", person.getName(), fieldWithoutErrors("name"), notNullOrEmpty(), containsIllegalWords("Everton", "Tavares")) //
				
				.check("name", person.getName().contains("José"), isTrue("mensagem padronizada"));

	}

}
