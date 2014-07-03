package gumga.framework.domain.repository;

public interface GumgaDeletableRepository<T> {
	
	public void delete(T model);

}
