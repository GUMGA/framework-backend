package gumga.framework.presentation;



public abstract class GumgaDTOAPI<T> extends AbstractGumgaAPI<T> {

	public GumgaDTOAPI(GumgaGateway<?, T> gateway) {
		super(gateway);
	}
	
}
