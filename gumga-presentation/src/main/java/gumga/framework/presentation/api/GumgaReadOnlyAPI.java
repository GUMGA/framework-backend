package gumga.framework.presentation.api;

import gumga.framework.domain.service.GumgaReadableServiceable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public abstract class GumgaReadOnlyAPI<T> extends AbstractReadOnlyGumgaAPI<T> {

	public GumgaReadOnlyAPI() {
		super(null);
	}
	
	@Autowired
	public void setService(GumgaReadableServiceable<T> service) {
		this.service = service;
	}

}
