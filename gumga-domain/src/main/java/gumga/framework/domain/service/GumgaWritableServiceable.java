package gumga.framework.domain.service;

public interface GumgaWritableServiceable<T> extends GumgaReadableServiceable<T> {
	
	public T save(T resource);
	
}
