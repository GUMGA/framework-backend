package gumga.framework.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import gumga.framework.core.GumgaThreadScope;
import static org.hibernate.criterion.Order.asc;
import static org.hibernate.criterion.Order.desc;
import static org.hibernate.criterion.Projections.rowCount;
import static org.hibernate.criterion.Restrictions.or;
import static org.hibernate.criterion.Restrictions.like;
import gumga.framework.core.QueryObject;
import gumga.framework.core.QueryObjectElement;
import gumga.framework.core.SearchResult;
import gumga.framework.domain.*;
import gumga.framework.domain.repository.GumgaCrudRepository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.CrudMethodMetadata;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public class GumgaGenericRepository<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements GumgaCrudRepository<T, ID> {

    protected final JpaEntityInformation<T, ID> entityInformation;
    protected final EntityManager entityManager;

    public GumgaGenericRepository(JpaEntityInformation<T, ID> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
        this.entityInformation = entityInformation;
    }

    @Override
    public SearchResult<T> search(QueryObject query) {

        if (query.isAdvanced()) {
            return advancedSearch(query);
        }

        Long count = count(query);
        List<T> data = getOrdered(query);

        return new SearchResult<T>(query, count, data);
    }

    private List<T> getOrdered(QueryObject query) {
        Pesquisa<T> pesquisa = getPesquisa(query);
        String sortField = query.getSortField();
        String sortType = query.getSortDir();

        if (!sortField.isEmpty()) {
            createAliasIfNecessary(pesquisa, sortField);
            pesquisa.addOrder(sortType.equals("asc") ? asc(sortField).ignoreCase() : desc(sortField).ignoreCase());
            pesquisa.addOrder(asc("id")); //GUMGA-478
        }

        return pesquisa.setFirstResult(query.getStart()).setMaxResults(query.getPageSize()).list();
    }

    private Long count(QueryObject query) {
        Object uniqueResult = getPesquisa(query).setProjection(rowCount()).uniqueResult();
        return uniqueResult == null ? 0L : ((Number) uniqueResult).longValue();
    }

    private Pesquisa<T> getPesquisa(QueryObject query) {
        if (query.getSearchFields() != null && query.getSearchFields().length == 0) {
            throw new IllegalArgumentException("Para realizar a search deve se informar pelo menos um campo a ser pesquisado.");
        }

        Criterion[] fieldsCriterions = new HibernateQueryObject(query).getCriterions(entityInformation.getJavaType());
        Pesquisa<T> pesquisa = search().add(or(fieldsCriterions));

        if (hasMultitenancy()) {
            String oiValue = (GumgaThreadScope.organizationCode.get() == null) ? "" : GumgaThreadScope.organizationCode.get();
            Criterion multitenancyCriterion = or(like("oi", oiValue, MatchMode.START), Restrictions.isNull("oi"));
            pesquisa.add(multitenancyCriterion);
        }

        if (query.getSearchFields() != null) {
            for (String field : query.getSearchFields()) {
                createAliasIfNecessary(pesquisa, field);
            }
        }

        return pesquisa;
    }

    public boolean hasMultitenancy() {
        return entityInformation.getJavaType().isAnnotationPresent(GumgaMultitenancy.class);
    }

    private void createAliasIfNecessary(Pesquisa<T> pesquisa, String field) {
        String[] chain = field.split("\\.");

        if (chain.length <= 1) {
            return;
        }
        if (pesquisa.getAliases().contains(chain[0])) {
            return;
        }

        pesquisa.createAlias(chain[0], chain[0]);
        pesquisa.addAlias(chain[0]);
    }

    @Override
    public Pesquisa<T> search() {
        return Pesquisa.createCriteria(session(), entityInformation.getJavaType());
    }

    @Override
    public T findOne(ID id) {
        T resource = super.findOne(id);

        if (resource == null) {
            throw new EntityNotFoundException("cannot find " + entityInformation.getJavaType() + " with id: " + id);
        }
        checkOwnership(resource);
        return resource;
    }

    private Session session() {
        return entityManager.unwrap(Session.class);
    }

    private SearchResult<T> advancedSearch(QueryObject query) {

        if (query.getAq().startsWith("{")) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                Map readValue = mapper.readValue(query.getAq(), Map.class);
                System.out.println("------------------------>"+readValue);
                query.setAq(readValue.get("aq").toString());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        List<QueryObjectElement> qoeFromString = GumgaGenericRepositoryHelper.qoeFromString(query.getAqo());
        String hqlFromQes = "";// GumgaGenericRepositoryHelper.hqlFromQoes(entityInformation,qoeFromString);

        if (!QueryObject.EMPTY.equals(query.getAqo())) {
            //query.setAq(hqlFromQes);
        }

        String modelo = "from %s obj WHERE %s";
        if (hasMultitenancy()) {
            modelo = "from %s obj WHERE (obj.oi is null OR obj.oi like '" + GumgaThreadScope.organizationCode.get() + "%%')  AND (%s) ";
        }

        String hqlConsulta = "";
        if (query.getSortField().isEmpty()) {
            hqlConsulta = String.format(modelo + " ORDER BY obj.id ", entityInformation.getEntityName(), query.getAq());
        } else {
            hqlConsulta = String.format(modelo + " ORDER BY %s %s, obj.id", entityInformation.getEntityName(), query.getAq(), query.getSortField(), query.getSortDir());
        }
        String hqlConta = String.format("SELECT count(obj) " + modelo, entityInformation.getEntityName(), query.getAq());
        Query qConta = entityManager.createQuery(hqlConta);
        Query qConsulta = entityManager.createQuery(hqlConsulta);
        Long total = (Long) qConta.getSingleResult();
        qConsulta.setMaxResults(query.getPageSize());
        qConsulta.setFirstResult(query.getStart());
        List resultList = qConsulta.getResultList();
        return new SearchResult<T>(query, total, resultList);
    }

    @Override
    public <A> SearchResult<A> advancedSearch(String selectQueryWithoutWhere, String countQuery, String ordenationId, QueryObject whereQuery) {
        if (Strings.isNullOrEmpty(ordenationId)) {
            throw new IllegalArgumentException("Para realizar a search deve se informar um OrdenationId para complementar a ordenação");
        }

        String modelo = selectQueryWithoutWhere + " WHERE %s";
        String hqlConsulta;
        if (whereQuery.getSortField().isEmpty()) {
            hqlConsulta = String.format(modelo, whereQuery.getAq());
        } else {
            hqlConsulta = String.format(modelo + " ORDER BY %s %s, %s", whereQuery.getAq(), whereQuery.getSortField(), whereQuery.getSortDir(), ordenationId);
        }
        String hqlConta = countQuery + " WHERE " + whereQuery.getAq();
        Query qConta = entityManager.createQuery(hqlConta);
        Query qConsulta = entityManager.createQuery(hqlConsulta);
        Long total = (Long) qConta.getSingleResult();
        qConsulta.setMaxResults(whereQuery.getPageSize());
        qConsulta.setFirstResult(whereQuery.getStart());
        List resultList = qConsulta.getResultList();

        return new SearchResult<A>(whereQuery, total, resultList);
    }

    public SearchResult<T> search(String hql, Map<String, Object> params) {
        Query query = entityManager.createQuery(hql);
        if (params != null) {
            for (String key : params.keySet()) {
                query.setParameter(key, params.get(key));
            }
        }
        List<T> result = query.getResultList();
        int total = result.size();
        return new SearchResult<T>(0, total, total, result);
    }

    @Override
    protected TypedQuery<Long> getCountQuery(Specification<T> spec) {
        return super.getCountQuery(spec);
    }

    @Override
    protected TypedQuery<T> getQuery(Specification<T> spec, Sort sort) {
        return super.getQuery(spec, sort);
    }

    @Override
    protected TypedQuery<T> getQuery(Specification<T> spec, Pageable pageable) {
        return super.getQuery(spec, pageable);
    }

    @Override
    protected Page<T> readPage(TypedQuery<T> query, Pageable pageable, Specification<T> spec) {
        return super.readPage(query, pageable, spec);
    }

    @Override
    public void flush() {
        super.flush();
    }

    @Override
    public <S extends T> List<S> save(Iterable<S> entities) {
        return super.save(entities);
    }

    @Override
    public <S extends T> S saveAndFlush(S entity) {
        return super.saveAndFlush(entity);
    }

    @Override
    public <S extends T> S save(S entity) {
        //setOi(entity);
        return super.save(entity);
    }

    @Override
    public long count(Specification<T> spec) {
        return super.count(spec);
    }

    @Override
    public long count() {
        return super.count();
    }

    @Override
    public List<T> findAll(Specification<T> spec, Sort sort) {
        if (hasMultitenancy()) {
            throw new RuntimeException(noMultiTenancyMessage());
        }
        return super.findAll(spec, sort);
    }

    @Override
    public Page<T> findAll(Specification<T> spec, Pageable pageable) {
        if (hasMultitenancy()) {
            throw new RuntimeException(noMultiTenancyMessage());
        }
        return super.findAll(spec, pageable);
    }

    @Override
    public List<T> findAll(Specification<T> spec) {
        if (hasMultitenancy()) {
            throw new RuntimeException(noMultiTenancyMessage());
        }
        return super.findAll(spec);
    }

    @Override
    public T findOne(Specification<T> spec) {
        if (hasMultitenancy()) {
            throw new RuntimeException(noMultiTenancyMessage());
        }
        return super.findOne(spec);
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        if (hasMultitenancy()) {
            throw new RuntimeException(noMultiTenancyMessage());
        }
        return super.findAll(pageable);
    }

    @Override
    public List<T> findAll(Sort sort) {
        if (hasMultitenancy()) {
            throw new RuntimeException(noMultiTenancyMessage());
        }
        return super.findAll(sort);
    }

    @Override
    public List<T> findAll(Iterable<ID> ids) {
        if (ids == null || !ids.iterator().hasNext()) {
            return Collections.emptyList();
        }
        List<T> toReturn = new ArrayList<>();
        for (ID id : ids) {
            Object found = findOne(id);
            if (found != null) {
                toReturn.add((T) found);
            }
        }
        return toReturn;
    }

    @Override
    public List<T> findAll() {
        if (hasMultitenancy()) {
            throw new RuntimeException(noMultiTenancyMessage());
        }
        return super.findAll();
    }

    @Override
    public boolean exists(ID id) {
        if (hasMultitenancy()) {
            throw new RuntimeException(noMultiTenancyMessage());
        }
        return super.exists(id);
    }

    @Override
    public T getOne(ID id) {
        if (hasMultitenancy()) {
            throw new RuntimeException(noMultiTenancyMessage());
        }
        return super.getOne(id);
    }

    @Override
    public void deleteAllInBatch() {
        if (hasMultitenancy()) {
            throw new RuntimeException(noMultiTenancyMessage());
        }
        super.deleteAllInBatch();
    }

    @Override
    public void deleteAll() {
        if (hasMultitenancy()) {
            throw new RuntimeException(noMultiTenancyMessage());
        }
        super.deleteAll();
    }

    @Override
    public void deleteInBatch(Iterable<T> entities) {
        if (hasMultitenancy()) {
            throw new RuntimeException(noMultiTenancyMessage());
        }
        super.deleteInBatch(entities);
    }

    @Override
    public void delete(Iterable<? extends T> entities) {
        super.delete(entities);
    }

    @Override
    public void delete(T entity) {
        super.delete(entity);
    }

    @Override
    public void delete(ID id) {
        super.delete(id);
    }

    @Override
    protected Class<T> getDomainClass() {
        return super.getDomainClass();
    }

    @Override
    public void setRepositoryMethodMetadata(CrudMethodMetadata crudMethodMetadata) {
        super.setRepositoryMethodMetadata(crudMethodMetadata);
    }

    @Override
    public List<GumgaObjectAndRevision> listOldVersions(ID id) {
        List<GumgaObjectAndRevision> aRetornar = new ArrayList<>();
        AuditReader ar = AuditReaderFactory.get(entityManager);
        List<Number> revisoes = ar.getRevisions(entityInformation.getJavaType(), id);
        for (Number n : revisoes) {
            GumgaRevisionEntity gumgaRevisionEntity = entityManager.find(GumgaRevisionEntity.class, n.longValue());
            T object = ar.find(entityInformation.getJavaType(), id, n.longValue());
            aRetornar.add(new GumgaObjectAndRevision(gumgaRevisionEntity, object));
        }
        return aRetornar;
    }

    private void checkOwnership(T o) throws EntityNotFoundException {
        if (GumgaThreadScope.ignoreCheckOwnership.get() != null && GumgaThreadScope.ignoreCheckOwnership.get()) {
            return;
        }
        if (!o.getClass().isAnnotationPresent(GumgaMultitenancy.class)) {
            return;
        }
        GumgaModel object = (GumgaModel) o;
        if (hasMultitenancy()) {
            if (object.getOi() != null) {
                if (GumgaThreadScope.organizationCode.get() == null || !object.getOi().getValue().startsWith(GumgaThreadScope.organizationCode.get())) {
                    throw new EntityNotFoundException("cannot find object of " + entityInformation.getJavaType() + " with id: " + object.getId() + " in your organization");
                }
            }
        }
    }

    private String noMultiTenancyMessage() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        String msg;
        if (stackTrace.length >= 4) {
            msg = "The method '" + stackTrace[2].getMethodName() + "' in line " + stackTrace[2].getLineNumber() + " of class '" + stackTrace[2].getClassName()
                    + "' called from method '" + stackTrace[3].getMethodName() + "' of class '" + stackTrace[3].getClassName()
                    + "' has no implementation for MultiTenancy yet. Ask Gumga.";
        } else {
            msg = "No implementation for MultiTenancy yet. Ask Gumga.";
        }
        return msg;
    }

}
