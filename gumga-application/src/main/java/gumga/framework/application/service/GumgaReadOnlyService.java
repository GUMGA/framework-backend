package gumga.framework.application.service;

import gumga.framework.application.customfields.GumgaCustomEnhancerService;
import gumga.framework.core.GumgaIdable;
import gumga.framework.core.QueryObject;
import gumga.framework.core.SearchResult;
import gumga.framework.domain.customfields.GumgaCustomizableModel;
import gumga.framework.domain.repository.GumgaCrudRepository;
import gumga.framework.domain.service.GumgaReadableServiceable;

import java.io.Serializable;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Scope("prototype")
public abstract class GumgaReadOnlyService<T extends GumgaIdable<?>, ID extends Serializable> extends AbstractGumgaService<T, ID> implements GumgaReadableServiceable<T> {

    public GumgaReadOnlyService(GumgaCrudRepository<T, ID> repository) {
        super(repository);
    }

    public void beforePesquisa(QueryObject query) {
    }

    public void afterPesquisa(SearchResult<T> result) {
    }

    @Transactional(readOnly = true)
    public SearchResult<T> pesquisa(QueryObject query) {
        beforePesquisa(query);
        SearchResult<T> result = repository.search(query);
        afterPesquisa(result);

        return result;
    }

    public void beforeView(ID id) {
    }

    public void afterView(T entity) {
    }

    @Transactional(readOnly = true)
    public T view(ID id) {
        beforeView(id);
        T entity = repository.findOne(id);
        loadGumgaCustomFields(entity);
        afterView(entity);
        return entity;
    }

}
