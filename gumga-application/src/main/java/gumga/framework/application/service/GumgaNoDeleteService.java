package gumga.framework.application.service;

import gumga.framework.core.GumgaIdable;
import gumga.framework.core.QueryObject;
import gumga.framework.core.SearchResult;
import gumga.framework.domain.repository.GumgaCrudRepository;
import gumga.framework.domain.service.GumgaReadableServiceable;
import gumga.framework.domain.service.GumgaWritableServiceable;
import gumga.framework.application.service.AbstractGumgaService;

import java.io.Serializable;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Scope("prototype")
public abstract class GumgaNoDeleteService<T extends GumgaIdable<?>, ID extends Serializable> extends AbstractGumgaService<T, ID> implements GumgaReadableServiceable<T>, GumgaWritableServiceable<T> {

	public GumgaNoDeleteService(GumgaCrudRepository<T, ID> repository) {
		super(repository);
	}

	public void beforePesquisa(QueryObject query) { }
	public void afterPesquisa(SearchResult<T> result) { }
	
	@Transactional(readOnly = true)
	public SearchResult<T> pesquisa(QueryObject query) {
		beforePesquisa(query);
		SearchResult<T> result = repository.pesquisa(query);
		afterPesquisa(result);
		
		return result;
	}
	
	public void beforeView(ID id) {}
	public void afterView(T entity) {}
	
	@Transactional(readOnly = true)
	public T view(ID id) {
		beforeView(id);
		T entity = repository.findOne(id);
		afterView(entity);
		
		return entity;
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
		T entity = repository.save(resource);
		afterSaveOrUpdate(entity);
		
		return entity;
	}
	
	public void forceFlush() {
		repository.flush();
	}
	
}
