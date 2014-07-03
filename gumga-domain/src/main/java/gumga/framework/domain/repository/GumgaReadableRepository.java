package gumga.framework.domain.repository;

import gumga.framework.domain.Inferable;

public interface GumgaReadableRepository<T> extends Inferable<T> {
	
	public T load(Long id);

}
