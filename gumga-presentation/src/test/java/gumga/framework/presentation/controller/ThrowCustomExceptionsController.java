package gumga.framework.presentation.controller;

import gumga.framework.core.exception.BadRequestException;
import gumga.framework.core.exception.ConflictException;
import gumga.framework.core.exception.ForbiddenException;
import gumga.framework.core.exception.NotFoundException;
import gumga.framework.core.exception.UnauthorizedException;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/teste")
public class ThrowCustomExceptionsController {

	@RequestMapping("/404")
	public void notFound() {
		throw new NotFoundException("Message defined on notFound exception");
	}

	@RequestMapping("/409")
	public void internal() {
		throw new ConflictException("Message defined on conflict exception");
	}

	@RequestMapping("/403")
	public void forbidden() {
		throw new ForbiddenException("Message defined on forbidden exception");
	}

	@RequestMapping("/401")
	public @ResponseBody String unauthorized() {
		throw new UnauthorizedException("Message defined on unauthorized exception");
	}
	
	@RequestMapping("/400")
	public @ResponseBody String badRequest() {
		throw new BadRequestException("Message defined on badRequest exception");
	}
	
	@RequestMapping("/500")
	public @ResponseBody String anyException() throws Exception {
		throw new Exception("Message defined on any exception");
	}
}
