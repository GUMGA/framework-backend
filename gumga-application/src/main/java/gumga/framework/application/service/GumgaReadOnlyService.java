package gumga.framework.application.service;

import javax.annotation.PostConstruct;

import gumga.framework.application.repository.GumgaReadOnlyRepository;
import gumga.framework.core.GumgaIdable;
import gumga.framework.core.QueryObject;
import gumga.framework.core.SearchResult;
import gumga.framework.domain.service.GumgaReadableServiceable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public abstract class GumgaReadOnlyService<T extends GumgaIdable<?>> extends AbstractGumgaService<T> implements GumgaReadableServiceable<T> {
	
	protected GumgaReadOnlyRepository<T> repository;
	
	@Autowired
	public void setRepository(GumgaReadOnlyRepository<T> repository) {
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
	
	@PostConstruct
	protected void failOver() {
		finder.setClazz(clazz());
		repository.setClazz(clazz());
	}

}
