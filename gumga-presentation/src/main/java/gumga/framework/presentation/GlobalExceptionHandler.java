package gumga.framework.presentation;

import gumga.framework.core.exception.BadRequestException;
import gumga.framework.core.exception.ConflictException;
import gumga.framework.core.exception.ForbiddenException;
import gumga.framework.core.exception.NotFoundException;
import gumga.framework.core.exception.UnauthorizedException;
import gumga.framework.presentation.validation.ErrorResource;
import gumga.framework.presentation.validation.FieldErrorResource;
import gumga.framework.validation.exception.InvalidEntityException;

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
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
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
		ErrorResource error = new ErrorResource("InvalidRequest", ex.getMessage());
		List<FieldError> fieldErrors = ex.getErrors().getFieldErrors();

		for (FieldError fieldError : fieldErrors) {
			FieldErrorResource fieldErrorResource = new FieldErrorResource();
			fieldErrorResource.setResource(fieldError.getObjectName());
			fieldErrorResource.setField(fieldError.getField());
			fieldErrorResource.setCode(fieldError.getCode());
			fieldErrorResource.setMessage(fieldError.getDefaultMessage());
			
			error.addFieldError(fieldErrorResource);
		}

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		logger.info("InvalidEntity", ex);
		return handleExceptionInternal(ex, error, headers, HttpStatus.UNPROCESSABLE_ENTITY, request);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public @ResponseBody
	ErrorResource illegalArgument(HttpServletRequest req, Exception ex) {
		logger.warn("IllegalArgument", ex);
		return new ErrorResource("IllegalArgument", "Invalid request", ex.getMessage());
	}

	@ExceptionHandler({ ConstraintViolationException.class, DataIntegrityViolationException.class })
	@ResponseStatus(HttpStatus.CONFLICT)
	public @ResponseBody
	ErrorResource constraintViolation(HttpServletRequest req, Exception ex) {
		logger.warn("Error on operation", ex);
		return new ErrorResource("ConstraintViolation", "Error on operation", ex.getMessage());
	}

	/*
	 * Tratamento das exceções padrão
	 */

	@ExceptionHandler({ EntityNotFoundException.class, NotFoundException.class, JpaObjectRetrievalFailureException.class })
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public @ResponseBody
	ErrorResource notFound(HttpServletRequest req, Exception ex) {
		logger.warn("ResourceNotFound", ex);
		return new ErrorResource("ResourceNotFound", "Entity not found", ex.getCause().getMessage());
	}

	@ExceptionHandler(BadRequestException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public @ResponseBody
	ErrorResource badRequest(HttpServletRequest req, Exception ex) {
		logger.warn("BadRequest", ex);
		return new ErrorResource("BadRequest", "Invalid request", ex.getMessage());
	}

	@ExceptionHandler({ ConflictException.class })
	@ResponseStatus(HttpStatus.CONFLICT)
	public @ResponseBody
	ErrorResource conflict(HttpServletRequest req, Exception ex) {
		logger.warn("Conflict", ex);
		return new ErrorResource("Conflict", "Error on operation", ex.getMessage());
	}

	@ExceptionHandler({ ForbiddenException.class })
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public @ResponseBody
	ErrorResource forbidden(HttpServletRequest req, Exception ex) {
		logger.warn("Forbidden", ex);
		return new ErrorResource("Forbidden", "Error on operation", ex.getMessage());
	}

	@ExceptionHandler({ UnauthorizedException.class })
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public @ResponseBody
	ErrorResource unauthorized(HttpServletRequest req, Exception ex) {
		logger.warn("Unauthorized", ex);
		return new ErrorResource("Unauthorized", "Error on operation", ex.getMessage());
	}

	/**
	 * Todo erro não tratado irá lançar um "INTERNAL SERVER ERROR", que corresponde ao status 500
	 * 
	 * @param req
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public @ResponseBody
	ErrorResource exception(HttpServletRequest req, Exception ex) {
		logger.error("Error on operation", ex);
		return new ErrorResource(ex.getClass().getSimpleName(), "Error on operation", ex.getMessage());
	}

}
