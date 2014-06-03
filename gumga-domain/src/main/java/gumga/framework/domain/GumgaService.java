package gumga.framework.domain;

import gumga.framework.core.QueryObject;
import gumga.framework.core.SearchResult;
import gumga.framework.core.utils.ReflectionUtils;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public abstract class GumgaService<T extends GumgaIdable> {
	
	@Autowired(required = false)
	protected GumgaFinder<T> finder;
	
	@Autowired(required = false)
	protected GumgaRepository<T> repository;
	
	public SearchResult<T> pesquisa(QueryObject queryObject) {
		return finder.pesquisa(queryObject);
	}
	
	public T view(Long id) {
		return finder.find(id);
	}
	
	public void delete(T resource) {
		repository.delete(resource);
	}
	
	public T save(T resource) {
		return repository.saveOrUpdate(resource);
	}
	
	@PostConstruct
	protected void failOver() {
		finder.setClazz(clazz());
		repository.setClazz(clazz());
	}
	
	@SuppressWarnings("unchecked")
	public Class<T> clazz() {
		return (Class<T>) ReflectionUtils.inferGenericType(getClass());
	}
	
}
