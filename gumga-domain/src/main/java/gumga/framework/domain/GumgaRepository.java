package gumga.framework.domain;

import gumga.framework.core.GumgaIdable;

public interface GumgaRepository<T extends GumgaIdable> extends Inferable<T> {
	
	public T saveOrUpdate(T model);
	
	public void delete(T model);
	
	public T load(Long id);
	
	public void flush();
	
	public void clear();
	
}
