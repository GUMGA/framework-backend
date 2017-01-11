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

    /**
     * Pesquisa na entidade tipada na interface @{@link GumgaReadableServiceable}
     * @param queryObject filtro da busca da entidade
     * @return
     */
    public SearchResult<T> pesquisa(QueryObject queryObject);

    /**
     * Pesquisa a entidade tipada na interface @{@link GumgaReadableServiceable} pela primary key
     * @param id
     * @return
     */
    public T view(Long id);

    public Class<T> clazz();

    /**
     * Retornar as versões anteriores das entidades marcadas pelas auditoria
     * @param id
     * @return
     */
    public List<GumgaObjectAndRevision> listOldVersions(Long id);

}
