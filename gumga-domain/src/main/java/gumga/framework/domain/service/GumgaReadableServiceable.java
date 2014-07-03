package gumga.framework.domain.service;

import gumga.framework.core.QueryObject;
import gumga.framework.core.SearchResult;

public interface GumgaReadableServiceable<T> {
	
	public SearchResult<T> pesquisa(QueryObject queryObject);
	
	public T view(Long id);
	
	public Class<T> clazz();

}
