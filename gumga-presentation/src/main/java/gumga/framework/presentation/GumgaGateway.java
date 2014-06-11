package gumga.framework.presentation;

import gumga.framework.core.GumgaIdable;
import gumga.framework.domain.GumgaService;

public abstract class GumgaGateway<T> {
	
	public abstract GumgaService<? extends GumgaIdable> service();
	
	public abstract GumgaTranslator<? extends GumgaIdable, T> translator();
	
}
