package gumga.framework.presentation.dto;

import gumga.framework.presentation.api.AbstractReadOnlyGumgaAPI;
import gumga.framework.presentation.gateway.GumgaReadOnlyGateway;

public abstract class GumgaReadOnlyDTOAPI<T> extends AbstractReadOnlyGumgaAPI<T> {
	
	public GumgaReadOnlyDTOAPI(GumgaReadOnlyGateway<?, T> gateway) {
		super(gateway);
	}

}
