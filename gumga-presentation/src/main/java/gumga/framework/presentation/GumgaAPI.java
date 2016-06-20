package gumga.framework.presentation;


import gumga.framework.application.GumgaService;
import gumga.framework.core.GumgaIdable;
import java.io.Serializable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public abstract class GumgaAPI<T extends GumgaIdable<ID>, ID extends Serializable> extends AbstractGumgaAPI<T> {
	
	public GumgaAPI(GumgaService<T, ID> service) {
		super(service);
	}
	
}
