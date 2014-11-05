package gumga.framework.application;

import static org.hibernate.criterion.Order.asc;
import static org.hibernate.criterion.Order.desc;
import static org.hibernate.criterion.Projections.rowCount;
import static org.hibernate.criterion.Restrictions.or;
import gumga.framework.core.QueryObject;
import gumga.framework.core.SearchResult;
import gumga.framework.domain.HibernateQueryObject;
import gumga.framework.domain.Pesquisa;
import gumga.framework.domain.repository.GumgaCrudRepository;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public class GumgaGenericRepository<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements GumgaCrudRepository<T, ID> {

    private final JpaEntityInformation<T, ID> entityInformation;
    private final EntityManager entityManager;

    public GumgaGenericRepository(JpaEntityInformation<T, ID> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
        this.entityInformation = entityInformation;
    }

    @Override
    public SearchResult<T> pesquisa(QueryObject query) {

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
            throw new IllegalArgumentException("Para realizar a pesquisa deve se informar pelo menos um campo a ser pesquisado.");
        }

        Criterion[] fieldsCriterions = new HibernateQueryObject(query).getCriterions(entityInformation.getJavaType());
        Pesquisa<T> pesquisa = pesquisa().add(or(fieldsCriterions));

        if (query.getSearchFields() != null) {
            for (String field : query.getSearchFields()) {
                createAliasIfNecessary(pesquisa, field);
            }
        }

        return pesquisa;
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
    public Pesquisa<T> pesquisa() {
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

}
