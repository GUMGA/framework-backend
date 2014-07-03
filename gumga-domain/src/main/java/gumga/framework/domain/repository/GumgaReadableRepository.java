package gumga.framework.domain.repository;

public interface GumgaReadableRepository<T> {
	
	public T load(Long id);

}
