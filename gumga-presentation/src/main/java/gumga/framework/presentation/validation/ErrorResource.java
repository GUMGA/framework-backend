package gumga.framework.presentation.validation;

import gumga.framework.presentation.RestResponse;

import java.util.List;

public class ErrorResource extends RestResponse<Object> {

	private List<FieldErrorResource> fieldErrors;

	public ErrorResource(String code, String message) {
		super(code, message);
	}

	public ErrorResource(String code, String message, String details) {
		super(code, message, details);
	}

	public List<FieldErrorResource> getFieldErrors() {
		return fieldErrors;
	}

	public void setFieldErrors(List<FieldErrorResource> fieldErrors) {
		this.fieldErrors = fieldErrors;
	}

}
