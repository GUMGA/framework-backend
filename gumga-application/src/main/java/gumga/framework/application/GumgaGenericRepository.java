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

import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public class GumgaGenericRepository<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements GumgaCrudRepository<T, ID> {

	private final Class<T> domainClass;
	private final EntityManager entityManager;
	
	public GumgaGenericRepository(Class<T> domainClass, EntityManager entityManager) {
		super(domainClass, entityManager);
		this.domainClass = domainClass;
		this.entityManager = entityManager;
	}

	@Override	
	public SearchResult<T> pesquisa(QueryObject query) {
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
		if (query.getSearchFields() != null && query.getSearchFields().length == 0)
			throw new IllegalArgumentException();
		
		Criterion[] fieldsCriterions = new HibernateQueryObject(query).getCriterions(domainClass);
		Pesquisa<T> pesquisa = pesquisa().add(or(fieldsCriterions));
		
		if (query.getSearchFields() != null) 
			for (String field : query.getSearchFields()) 
				createAliasIfNecessary(pesquisa, field);
		
		return pesquisa;
	}
	
	private void createAliasIfNecessary(Pesquisa<T> pesquisa, String field) {
		String[] chain = field.split("\\.");
		
		if (chain.length <= 1) return;
		if (pesquisa.getAliases().contains(chain[0])) return;
		
		pesquisa.createAlias(chain[0], chain[0]);
		pesquisa.addAlias(chain[0]);
	}
	
	@Override
	public Pesquisa<T> pesquisa() {
		return Pesquisa.createCriteria(session(), domainClass);
	}

	private Session session() {
		return entityManager.unwrap(Session.class);
	}
	
}
