package gumga.framework.domain.repository;

import gumga.framework.core.QueryObject;
import gumga.framework.core.SearchResult;
import gumga.framework.domain.GumgaObjectAndRevision;
import gumga.framework.domain.GumgaRepository;
import gumga.framework.domain.Pesquisa;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface GumgaCrudRepository<T, ID extends Serializable> extends JpaRepository<T, ID>, GumgaRepository<T, ID> {

    SearchResult<T> search(QueryObject query);

    Pesquisa<T> search();

    SearchResult<T> search(String hql, Map<String, Object> params);

    List<GumgaObjectAndRevision> listOldVersions(ID id);

    <A> SearchResult<A> advancedSearch(String selectQueryWithoutWhere, String countObjt, String ordenationId, QueryObject whereQuery);

}
