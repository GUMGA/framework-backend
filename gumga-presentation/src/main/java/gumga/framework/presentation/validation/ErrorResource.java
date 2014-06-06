package gumga.framework.presentation.validation;

import java.util.List;

public class ErrorResource {

	private String code;
	private String message;
	private String details;
	private List<FieldErrorResource> fieldErrors;

	public ErrorResource() {
	}
	
	public ErrorResource(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public ErrorResource(String code, String message, String details) {
		this.code = code;
		this.message = message;
		this.details = details;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public List<FieldErrorResource> getFieldErrors() {
		return fieldErrors;
	}

	public void setFieldErrors(List<FieldErrorResource> fieldErrors) {
		this.fieldErrors = fieldErrors;
	}

}
