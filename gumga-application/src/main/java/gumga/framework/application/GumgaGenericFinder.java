package gumga.framework.application;

import static org.hibernate.criterion.Order.asc;
import static org.hibernate.criterion.Order.desc;
import static org.hibernate.criterion.Projections.rowCount;
import static org.hibernate.criterion.Restrictions.or;
import gumga.framework.core.GumgaIdable;
import gumga.framework.core.QueryObject;
import gumga.framework.core.SearchResult;
import gumga.framework.core.utils.ReflectionUtils;
import gumga.framework.domain.GumgaFinder;
import gumga.framework.domain.HibernateQueryObject;
import gumga.framework.domain.Pesquisa;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.mysema.query.jpa.hibernate.HibernateQuery;

@Component
@Scope("prototype")
public class GumgaGenericFinder<T extends GumgaIdable> implements GumgaFinder<T> {
	
	@Autowired
	private GumgaSessionStrategy sessionStrategy;
	
	private Class<T> clazz;
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	private Session session() {
		return sessionStrategy.getSession();
	}
	
	@SuppressWarnings("unchecked")
	private T firstOrNull(List<?> list) {
		return (T) (list.isEmpty() ? null : list.get(0));
	}

	@Transactional(readOnly = true)
	public T first() {
		Criteria criteria = session().createCriteria(clazz()).addOrder(Order.asc("id")).setMaxResults(1);
		return firstOrNull(criteria.list());
	}

	@Transactional(readOnly = true)
	public T last() {
		Criteria criteria = session().createCriteria(clazz()).addOrder(Order.desc("id")).setMaxResults(1);
		return firstOrNull(criteria.list());
	}

	@Transactional(readOnly = true)
	public T find(Long id) {
		T entity = clazz().cast(session().get(clazz(), id));
		
		if (entity == null) 
			throw new EntityNotFoundException(clazz().getSimpleName() + " with id " + id);
		
		return entity;
	}

	@Transactional(readOnly = true)
	public List<T> all() {
		return pesquisa().list();
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<T> find(Long... id) {
		Criteria criteria = session().createCriteria(clazz()).add(Restrictions.in("id", id));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<T> limit(int limit) {
		return session().createCriteria(clazz()).setMaxResults(limit).list();
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<T> offset(int limit, int offset) {
		return session().createCriteria(clazz()).setFirstResult(offset).setMaxResults(limit).list();
	}
	
	@SuppressWarnings("unchecked")
	public Class<T> clazz() {
		if (clazz == null) 
			clazz = (Class<T>) ReflectionUtils.inferGenericType(getClass());
		
		return clazz;
	}
	
	@Transactional(readOnly = true)
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
		
		Criterion[] fieldsCriterions = new HibernateQueryObject(query).getCriterions(clazz());
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
	
	@Transactional(readOnly = true)
	public Pesquisa<T> pesquisa() {
		return Pesquisa.createCriteria(session(), clazz());
	}
	
	@Transactional(readOnly = true)
	public Query createQuery(String query) {
		return session().createQuery(query);
	}
	
	@Transactional(readOnly = true)
	public SQLQuery createSQLQuery(String query) {
		return session().createSQLQuery(query);
	}
	
	@Transactional(readOnly = true)
	public HibernateQuery queryDSL() {
		return new HibernateQuery(session());
	}
	
	@Transactional(readOnly = true)
	public Long count() {
		Number count = (Number) pesquisa().setProjection(rowCount()).uniqueResult();
		return count.longValue();
	}
	
	public void setClazz(Class<T> clazz) {
		this.clazz = clazz;
	}
	
}
