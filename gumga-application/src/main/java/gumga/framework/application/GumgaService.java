package gumga.framework.application;

import gumga.framework.core.GumgaIdable;
import gumga.framework.core.QueryObject;
import gumga.framework.core.SearchResult;
import gumga.framework.core.utils.ReflectionUtils;
import gumga.framework.domain.GumgaFinder;
import gumga.framework.domain.GumgaRepository;
import gumga.framework.domain.GumgaServiceable;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public abstract class GumgaService<T extends GumgaIdable> implements GumgaServiceable<T> {
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	protected GumgaFinder<T> finder;
	protected GumgaRepository<T> repository;
	
	@Autowired
	public void setFinder(GumgaFinder<T> finder) {
		this.finder = finder;
	}
	
	@Autowired
	public void setRepository(GumgaRepository<T> repository) {
		this.repository = repository;
	}
	
	public void beforePesquisa(QueryObject query) { }
	public void afterPesquisa(SearchResult<T> result) { }
	
	public SearchResult<T> pesquisa(QueryObject query) {
		beforePesquisa(query);
		SearchResult<T> result = finder.pesquisa(query);
		afterPesquisa(result);
		
		return result;
	}
	
	public void beforeView(Long id) {}
	public void afterView(T entity) {}
	
	public T view(Long id) {
		beforeView(id);
		T entity = finder.find(id);
		afterView(entity);
		
		return entity;
	}
	
	public void beforeDelete(T entity) {}
	public void afterDelete() {}
	
	public void delete(T resource) {
		beforeDelete(resource);
		repository.delete(resource);
		afterDelete();
	}
	
	public void beforeSave(T entity) {}
	public void afterSave(T entity) {}
	
	public T save(T resource) {
		beforeSave(resource);
		T entity = repository.saveOrUpdate(resource);
		afterSave(entity);
		
		return entity;
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
