package gumga.framework.domain.repository;

import gumga.framework.domain.Inferable;

public interface GumgaDeletableRepository<T> extends Inferable<T> {
	
	public void delete(T model);

}
