package gumga.framework.application;

import gumga.framework.core.GumgaThreadScope;
import static org.hibernate.criterion.Order.asc;
import static org.hibernate.criterion.Order.desc;
import static org.hibernate.criterion.Projections.rowCount;
import static org.hibernate.criterion.Restrictions.or;
import static org.hibernate.criterion.Restrictions.and;
import static org.hibernate.criterion.Restrictions.like;
import gumga.framework.core.QueryObject;
import gumga.framework.core.SearchResult;
import gumga.framework.domain.*;
import gumga.framework.domain.domains.GumgaDomain;
import gumga.framework.domain.domains.GumgaOi;
import gumga.framework.domain.repository.GumgaCrudRepository;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
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
    private final EntityManager entityManager;

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
            Criterion multitenancyCriterion = or(like("oi", GumgaThreadScope.organizationCode.get(), MatchMode.START), Restrictions.isNull("oi"));
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

        return resource;
    }

    private Session session() {
        return entityManager.unwrap(Session.class);
    }

    private SearchResult<T> advancedSearch(QueryObject query) {
        String modelo = "from %s obj WHERE %s";
        if (hasMultitenancy()) {
            modelo = "from %s obj WHERE (obj.oi is null OR obj.oi like '" + GumgaThreadScope.organizationCode.get() + "%%')  AND (%s) ";
        }

        String hqlConsulta = "";
        if (query.getSortField().isEmpty()) {
            hqlConsulta = String.format(modelo, entityInformation.getEntityName(), query.getAq());
        } else {
            hqlConsulta = String.format(modelo + " ORDER BY %s %s", entityInformation.getEntityName(), query.getAq(), query.getSortField(), query.getSortDir());
        }
        String hqlConta = String.format("SELECT count(obj) " + modelo, entityInformation.getEntityName(), query.getAq());
        System.out.println("HQL Consulta:" + hqlConsulta);
        System.out.println("HQL Conta:" + hqlConta);
        Query qConta = entityManager.createQuery(hqlConta);
        Query qConsulta = entityManager.createQuery(hqlConsulta);
        Long total = (Long) qConta.getSingleResult();
        qConsulta.setMaxResults(query.getPageSize());
        qConsulta.setFirstResult(query.getStart());
        List resultList = qConsulta.getResultList();
        return new SearchResult<T>(query, total, resultList);
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
        return super.getCountQuery(spec); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected TypedQuery<T> getQuery(Specification<T> spec, Sort sort) {
        return super.getQuery(spec, sort); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected TypedQuery<T> getQuery(Specification<T> spec, Pageable pageable) {
        return super.getQuery(spec, pageable); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected Page<T> readPage(TypedQuery<T> query, Pageable pageable, Specification<T> spec) {
        return super.readPage(query, pageable, spec); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void flush() {
        super.flush(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <S extends T> List<S> save(Iterable<S> entities) {
        return super.save(entities); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <S extends T> S saveAndFlush(S entity) {
        return super.saveAndFlush(entity); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <S extends T> S save(S entity) {
        setOi(entity);
        return super.save(entity); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public long count(Specification<T> spec) {
        return super.count(spec); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public long count() {
        return super.count(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<T> findAll(Specification<T> spec, Sort sort) {
        return super.findAll(spec, sort); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Page<T> findAll(Specification<T> spec, Pageable pageable) {
        return super.findAll(spec, pageable); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<T> findAll(Specification<T> spec) {
        return super.findAll(spec); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public T findOne(Specification<T> spec) {
        return super.findOne(spec); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        return super.findAll(pageable); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<T> findAll(Sort sort) {
        return super.findAll(sort); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<T> findAll(Iterable<ID> ids) {
        return super.findAll(ids); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<T> findAll() {
        return super.findAll(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean exists(ID id) {
        return super.exists(id); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public T getOne(ID id) {
        return super.getOne(id); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteAllInBatch() {
        super.deleteAllInBatch(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteAll() {
        super.deleteAll(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteInBatch(Iterable<T> entities) {
        super.deleteInBatch(entities); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(Iterable<? extends T> entities) {
        super.delete(entities); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(T entity) {
        super.delete(entity); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(ID id) {
        super.delete(id); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected Class<T> getDomainClass() {
        return super.getDomainClass(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setRepositoryMethodMetadata(CrudMethodMetadata crudMethodMetadata) {
        super.setRepositoryMethodMetadata(crudMethodMetadata); //To change body of generated methods, choose Tools | Templates.
    }

    private <S extends T> void setOi(S entity) {
        try {
            GumgaModel model = GumgaModel.class.cast(entity);

            if (model.getOi() == null && model.getId() == null) {
                Field fieldOi = GumgaModel.class.getDeclaredField("oi");
                fieldOi.setAccessible(true);
                fieldOi.set(entity, new GumgaOi(GumgaThreadScope.organizationCode.get()));
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
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

    @Override
    public <A> SearchResult<A> advancedSearch(String selectQueryWithoutWhere, String countQuery, QueryObject whereQuery) {
        String modelo = selectQueryWithoutWhere + " WHERE %s";
        String hqlConsulta;
        if (whereQuery.getSortField().isEmpty()) {
            hqlConsulta = String.format(modelo, whereQuery.getAq());
        } else {
            hqlConsulta = String.format(modelo + " ORDER BY %s %s", whereQuery.getAq(), whereQuery.getSortField(), whereQuery.getSortDir());
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

}
