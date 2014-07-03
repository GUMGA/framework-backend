package gumga.framework.domain.repository;

import gumga.framework.domain.Inferable;

public interface GumgaWritableRepository<T> extends Inferable<T> {
	
	public T saveOrUpdate(T model);
	
	public void flush();
	
	public void clear();

}
