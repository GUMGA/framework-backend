package gumga.framework.domain.exception;

import org.springframework.validation.Errors;

public class InvalidEntityException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private final Errors errors;
	
	public InvalidEntityException(Errors errors) {
		this.errors = errors;
	}
	
	public Errors getErrors() {
		return errors;
	}

}
