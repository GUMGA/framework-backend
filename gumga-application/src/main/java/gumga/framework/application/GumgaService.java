package gumga.framework.application;

import gumga.framework.application.service.AbstractGumgaService;
import gumga.framework.core.GumgaIdable;
import gumga.framework.core.QueryObject;
import gumga.framework.core.SearchResult;
import gumga.framework.domain.GumgaObjectAndRevision;
import gumga.framework.domain.GumgaServiceable;
import gumga.framework.domain.repository.GumgaCrudRepository;

import java.io.Serializable;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Scope("prototype")
public abstract class GumgaService<T extends GumgaIdable<ID>, ID extends Serializable> extends AbstractGumgaService<T, ID> implements GumgaServiceable<T> {

    public GumgaService(GumgaCrudRepository<T, ID> repository) {
        super(repository);
    }

    public void beforePesquisa(QueryObject query) {
    }

    public void afterPesquisa(SearchResult<T> result) {
    }

    @Transactional(readOnly = true)
    public SearchResult<T> pesquisa(QueryObject query) {
        beforePesquisa(query);
        SearchResult<T> result = repository.pesquisa(query);
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
        afterView(entity);

        return entity;
    }

    public void beforeDelete(T entity) {
    }

    public void afterDelete() {
    }

    @Transactional
    public void delete(T resource) {
        beforeDelete(resource);
        repository.delete(resource);
        afterDelete();
    }

    private void beforeSaveOrUpdate(T entity, boolean isNew) {
        if (isNew) {
            beforeSave(entity);
        } else {
            beforeUpdate(entity);
        }
    }

    private void afterSaveOrUpdate(T entity, boolean isNew) {
        if (isNew) {
            afterSave(entity);
        } else {
            afterUpdate(entity);
        }
    }

    public void beforeSave(T entity) {
    }

    public void beforeUpdate(T entity) {
    }

    public void afterSave(T entity) {
    }

    public void afterUpdate(T entity) {
    }

    @Transactional
    public T save(T resource) {
        boolean isNew = (resource.getId() == null);

        beforeSaveOrUpdate(resource, isNew);
        T entity = repository.save(resource);
        afterSaveOrUpdate(entity, isNew);

        return entity;
    }

    public void forceFlush() {
        repository.flush();
    }

    @Transactional(readOnly = true)
    public List<GumgaObjectAndRevision> listOldVersions(ID id) {
        List<GumgaObjectAndRevision> oldVersions = repository.listOldVersions(id);
        return oldVersions;
    }

}
