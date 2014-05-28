package gumga.framework.domain;

import static org.hibernate.criterion.Order.asc;
import static org.hibernate.criterion.Order.desc;
import static org.hibernate.criterion.Projections.rowCount;
import static org.hibernate.criterion.Restrictions.or;
import gumga.framework.core.QueryObject;
import gumga.framework.core.SearchResult;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityNotFoundException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
public abstract class GumgaRepository<T extends GumgaIdable> {
	
	@Autowired
	private SessionFactory sessionFactory;
	private Class<T> clazz;
	
	protected Logger logger = Logger.getLogger(getClass().getSimpleName());
	
	public Session session() {
		return sessionFactory.getCurrentSession();
	}
	
	@Transactional(readOnly = true)
	public List<T> all() {
		return pesquisa().list();
	}
	
	@Transactional(propagation = Propagation.MANDATORY)
	public T saveOrUpdate(T model) {
		session().saveOrUpdate(model);
		return model;
	}
	
	@Transactional(propagation = Propagation.MANDATORY)
	public void delete(GumgaModel model) {
		session().delete(model);
	}
	
	@Transactional(readOnly = true)
	public T load(Long id) {
		T entity = clazz().cast(session().get(clazz(), id));
		
		if (entity == null) 
			throw new EntityNotFoundException(clazz().getSimpleName() + " with id " + id);
		
		return entity;
	}
	
	@Transactional(readOnly = true)
	public Long count() {
		Number count = (Number) pesquisa().setProjection(rowCount()).uniqueResult();
		return count.longValue();
	}
	
	@Transactional(readOnly = true)
	public Pesquisa<T> pesquisa() {
		return Pesquisa.createCriteria(session(), clazz());
	}
	
	@SuppressWarnings("unchecked")
	public Class<T> clazz() {
		if (clazz == null) {
			Type mySuperclass = getClass().getGenericSuperclass();
			clazz = (Class<T>) ((ParameterizedType) mySuperclass).getActualTypeArguments()[0];
		}
		
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
	
	protected Pesquisa<T> getPesquisa(QueryObject query) {
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
	
}
