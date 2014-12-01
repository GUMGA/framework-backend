package gumga.framework.domain.repository;

import gumga.framework.core.QueryObject;
import gumga.framework.core.SearchResult;
import gumga.framework.domain.GumgaRepository;
import gumga.framework.domain.Pesquisa;

import java.io.Serializable;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface GumgaCrudRepository<T, ID extends Serializable> extends JpaRepository<T, ID>, GumgaRepository<T, ID> {

    SearchResult<T> pesquisa(QueryObject query);

    Pesquisa<T> pesquisa();

    public SearchResult<T> hqlSearch(String hql, Map<String, Object> params);

}
