package gumga.framework.presentation.api;

import gumga.framework.core.QueryObject;
import gumga.framework.core.SearchResult;
import gumga.framework.domain.GumgaServiceable;
import gumga.framework.domain.service.GumgaReadableServiceable;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public abstract class AbstractReadOnlyGumgaAPI<T> extends AbstractProtoGumgaAPI<T> {
	
	protected GumgaReadableServiceable<T> service;
	
	public AbstractReadOnlyGumgaAPI(GumgaReadableServiceable<T> service) {
		this.service = service;
	}
	
	@RequestMapping
	public SearchResult<T> pesquisa(QueryObject query) {
		SearchResult<T> pesquisa = service.pesquisa(query);
		return new SearchResult<>(query, pesquisa.getCount(), pesquisa.getValues());
	}
	
	@RequestMapping("/{id}")
	public T load(@PathVariable Long id) {
		return service.view(id);
	}
	
	public void setService(GumgaServiceable<T> service) {
		this.service = service;
	}

}
