package gumga.framework.domain.repository;

public interface GumgaWritableRepository<T> {
	
	public T saveOrUpdate(T model);
	
	public void flush();
	
	public void clear();

}
