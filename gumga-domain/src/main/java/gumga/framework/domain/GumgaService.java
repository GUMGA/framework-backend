package gumga.framework.domain;

import gumga.framework.core.GumgaIdable;
import gumga.framework.core.QueryObject;
import gumga.framework.core.SearchResult;
import gumga.framework.core.utils.ReflectionUtils;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public abstract class GumgaService<T extends GumgaIdable> implements IGumgaService<T> {
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	protected GumgaFinder<T> finder;
	protected GumgaRepository<T> repository;
	
	@Autowired(required = false)
	public void setFinder(GumgaFinder<T> finder) {
		this.finder = finder;
	}
	
	@Autowired(required = false)
	public void setRepository(GumgaRepository<T> repository) {
		this.repository = repository;
	}
	
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
	
	public void forceFlush() {
		repository.flush();
		repository.clear();
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
