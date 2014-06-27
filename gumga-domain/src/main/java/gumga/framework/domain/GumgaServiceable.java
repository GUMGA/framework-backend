package gumga.framework.domain;

import gumga.framework.core.QueryObject;
import gumga.framework.core.SearchResult;

public interface GumgaServiceable<T> {
	
	public SearchResult<T> pesquisa(QueryObject queryObject);
	
	public T view(Long id);
	
	public void delete(T resource);
	
	public T save(T resource);
	
	public Class<T> clazz();

}
