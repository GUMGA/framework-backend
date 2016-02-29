package gumga.framework.domain.service;

import gumga.framework.core.QueryObject;
import gumga.framework.core.SearchResult;
import gumga.framework.domain.GumgaObjectAndRevision;
import java.util.List;

/**
 * Service com a operação de view, find, obter a classe e listas as versões
 * anteriores
 */
public interface GumgaReadableServiceable<T> {

    public SearchResult<T> pesquisa(QueryObject queryObject);

    public T view(Long id);

    public Class<T> clazz();

    public List<GumgaObjectAndRevision> listOldVersions(Long id);

}
