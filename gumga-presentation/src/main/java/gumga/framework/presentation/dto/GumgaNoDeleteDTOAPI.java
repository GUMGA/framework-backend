package gumga.framework.presentation.dto;

import gumga.framework.presentation.api.AbstractNoDeleteGumgaAPI;
import gumga.framework.presentation.gateway.GumgaNoDeleteGateway;

public abstract class GumgaNoDeleteDTOAPI<T> extends AbstractNoDeleteGumgaAPI<T> {
	
	public GumgaNoDeleteDTOAPI(GumgaNoDeleteGateway<?, T> gateway) {
		super(gateway);
	}

}
