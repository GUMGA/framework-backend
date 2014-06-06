package gumga.framework.presentation;


import gumga.framework.presentation.validation.ErrorResource;
import gumga.framework.presentation.validation.FieldErrorResource;
import gumga.framework.validation.exception.InvalidEntityException;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
	
	private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	@ExceptionHandler(InvalidEntityException.class)
	public ResponseEntity<Object> handleCustomException(InvalidEntityException ex, WebRequest request) {
		
		List<FieldErrorResource> fieldErrorResources = new ArrayList<>();

        List<FieldError> fieldErrors = ex.getErrors().getFieldErrors();
        
        for (FieldError fieldError : fieldErrors) {
            FieldErrorResource fieldErrorResource = new FieldErrorResource();
            fieldErrorResource.setResource(fieldError.getObjectName());
            fieldErrorResource.setField(fieldError.getField());
            fieldErrorResource.setCode(fieldError.getCode());
            fieldErrorResource.setMessage(fieldError.getDefaultMessage());
            fieldErrorResources.add(fieldErrorResource);
        }

        ErrorResource error = new ErrorResource("InvalidRequest", ex.getMessage());
        error.setFieldErrors(fieldErrorResources);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        logger.info("InvalidEntity", ex);
        return handleExceptionInternal(ex, error, headers, HttpStatus.UNPROCESSABLE_ENTITY, request);
	}
	
	@ExceptionHandler(EntityNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public @ResponseBody ErrorResource notFound(HttpServletRequest req, Exception ex) {
		logger.warn("ResourceNotFound", ex);
		return new ErrorResource("EntityNotFound", "Entity not found", ex.getMessage());
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public @ResponseBody ErrorResource badRequest(HttpServletRequest req, Exception ex) {
		logger.warn("BadRequest", ex);
		return new ErrorResource("IllegalArgument", "Invalid request", ex.getMessage());
	}
	
	@ExceptionHandler({ConstraintViolationException.class, DataIntegrityViolationException.class})
	@ResponseStatus(HttpStatus.CONFLICT)
	public @ResponseBody ErrorResource constraintViolation(HttpServletRequest req, Exception ex) {
		logger.warn("Error on operation", ex);
		return new ErrorResource("ConstraintViolation", "Error on operation", ex.getMessage());
	}
	
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public @ResponseBody ErrorResource exception(HttpServletRequest req, Exception ex) {
		logger.error("Error on operation", ex);
		return new ErrorResource(ex.getClass().getSimpleName(), "Error on operation", ex.getMessage());
	}

}
