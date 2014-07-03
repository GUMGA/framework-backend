package gumga.framework.application;

import gumga.framework.application.service.AbstractGumgaService;
import gumga.framework.core.GumgaIdable;
import gumga.framework.core.QueryObject;
import gumga.framework.core.SearchResult;
import gumga.framework.domain.GumgaRepository;
import gumga.framework.domain.GumgaServiceable;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Scope("prototype")
public abstract class GumgaService<T extends GumgaIdable<?>> extends AbstractGumgaService<T> implements GumgaServiceable<T> {
	
	protected GumgaRepository<T> repository;
	
	@Autowired
	public void setRepository(GumgaRepository<T> repository) {
		this.repository = repository;
	}
	
	public void beforePesquisa(QueryObject query) { }
	public void afterPesquisa(SearchResult<T> result) { }
	
	@Transactional(readOnly = true)
	public SearchResult<T> pesquisa(QueryObject query) {
		beforePesquisa(query);
		SearchResult<T> result = finder.pesquisa(query);
		afterPesquisa(result);
		
		return result;
	}
	
	public void beforeView(Long id) {}
	public void afterView(T entity) {}
	
	@Transactional(readOnly = true)
	public T view(Long id) {
		beforeView(id);
		T entity = finder.find(id);
		afterView(entity);
		
		return entity;
	}
	
	public void beforeDelete(T entity) {}
	public void afterDelete() {}
	
	@Transactional
	public void delete(T resource) {
		beforeDelete(resource);
		repository.delete(resource);
		afterDelete();
	}
	
	private void beforeSaveOrUpdate(T entity) {
		if (entity.getId() == null)
			beforeSave(entity);
		else
			beforeUpdate(entity);
	}
	
	private void afterSaveOrUpdate(T entity) {
		if (entity.getId() == null)
			afterSave(entity);
		else
			afterUpdate(entity);
	}
	
	public void beforeSave(T entity) {}
	public void beforeUpdate(T entity) {}
	public void afterSave(T entity) {}
	public void afterUpdate(T entity) {}
	
	@Transactional
	public T save(T resource) {
		beforeSaveOrUpdate(resource);
		T entity = repository.saveOrUpdate(resource);
		afterSaveOrUpdate(entity);
		
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
	
}
