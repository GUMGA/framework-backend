package gumga.framework.domain.service;

import gumga.framework.core.QueryObject;
import gumga.framework.core.SearchResult;
import gumga.framework.domain.GumgaObjectAndRevision;
import java.util.List;

public interface GumgaReadableServiceable<T> {

    public SearchResult<T> pesquisa(QueryObject queryObject);

    public T view(Long id);

    public Class<T> clazz();

    public List<GumgaObjectAndRevision> listOldVersions(Long id);

}
