package gumga.framework.presentation.api;

import gumga.framework.domain.service.GumgaWritableServiceable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public abstract class GumgaNoDeleteAPI<T> extends AbstractNoDeleteGumgaAPI<T> {

	public GumgaNoDeleteAPI() {
		super(null);
	}
	
	@Autowired
	public void setService(GumgaWritableServiceable<T> service) {
		setService(service);
	}

}
