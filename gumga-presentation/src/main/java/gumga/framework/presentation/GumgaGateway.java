package gumga.framework.presentation;

import gumga.framework.core.GumgaIdable;
import gumga.framework.core.QueryObject;
import gumga.framework.core.SearchResult;
import gumga.framework.core.utils.ReflectionUtils;
import gumga.framework.domain.IGumgaService;

import java.util.List;

public abstract class GumgaGateway<A extends GumgaIdable, DTO> implements IGumgaService<DTO> {
	
	private final IGumgaService<A> delegate;
	private final GumgaTranslator<A, DTO> translator;
	
	public GumgaGateway(IGumgaService<A> delegate, GumgaTranslator<A, DTO> translator) {
		this.delegate = delegate;
		this.translator = translator;
	}

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
