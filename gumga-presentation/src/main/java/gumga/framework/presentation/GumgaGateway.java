package gumga.framework.presentation;

import gumga.framework.application.GumgaService;
import gumga.framework.core.GumgaIdable;
import gumga.framework.core.QueryObject;
import gumga.framework.core.SearchResult;
import gumga.framework.core.utils.ReflectionUtils;
import gumga.framework.domain.GumgaServiceable;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

public abstract class GumgaGateway<A extends GumgaIdable<?>, DTO> implements GumgaServiceable<DTO> {
	
	@Autowired
	protected GumgaService<A> delegate;
	
	@Autowired
	protected GumgaTranslator<A, DTO> translator;
	
	@Override
	public SearchResult<DTO> pesquisa(QueryObject query) {
		SearchResult<A> pesquisa = delegate.pesquisa(query);
		return new SearchResult<DTO>(query, pesquisa.getCount(), translator.from((List<A>) pesquisa.getValues()));
	}

	@Override
	public DTO view(Long id) {
		return translator.from(delegate.view(id));
	}

	@Override
	public void delete(DTO resource) {
		delegate.delete(translator.to(resource));
	}

	@Override
	public DTO save(DTO resource) {
		return translator.from(delegate.save(translator.to(resource)));
	}
	
	@SuppressWarnings("unchecked")
	public Class<DTO> clazz() {
		return (Class<DTO>) ReflectionUtils.inferGenericType(getClass());
	}

}
