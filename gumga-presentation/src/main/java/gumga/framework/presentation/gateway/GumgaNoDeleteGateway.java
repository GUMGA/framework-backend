package gumga.framework.presentation.gateway;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import gumga.framework.application.GumgaService;
import gumga.framework.core.GumgaIdable;
import gumga.framework.core.QueryObject;
import gumga.framework.core.SearchResult;
import gumga.framework.core.utils.ReflectionUtils;
import gumga.framework.domain.service.GumgaReadableServiceable;
import gumga.framework.domain.service.GumgaWritableServiceable;
import gumga.framework.presentation.GumgaTranslator;

public abstract class GumgaNoDeleteGateway<A extends GumgaIdable<?>, DTO> implements GumgaReadableServiceable<DTO>, GumgaWritableServiceable<DTO> {

	@Autowired
	private GumgaService<A, ?> delegate;
	
	@Autowired
	private GumgaTranslator<A, DTO> translator;
	
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
	public DTO save(DTO resource) {
		return translator.from(delegate.save(translator.to(resource)));
	}
	
	@SuppressWarnings("unchecked")
	public Class<DTO> clazz() {
		return (Class<DTO>) ReflectionUtils.inferGenericType(getClass());
	}

}
