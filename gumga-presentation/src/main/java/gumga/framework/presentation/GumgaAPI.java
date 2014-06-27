package gumga.framework.presentation;


import gumga.framework.application.GumgaService;
import gumga.framework.core.GumgaIdable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public abstract class GumgaAPI<T extends GumgaIdable> extends AbstractGumgaAPI<T> {
	
	public GumgaAPI() {
		super(null);
	}
	
	@Autowired
	public void setService(GumgaService<T> service) {
		this.service = service;
	}
	
}
