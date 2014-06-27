package gumga.framework.application;

import gumga.framework.core.GumgaIdable;
import gumga.framework.core.utils.ReflectionUtils;
import gumga.framework.domain.GumgaRepository;

import javax.persistence.EntityNotFoundException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Scope("prototype")
public class GumgaGenericRepository<T extends GumgaIdable> implements GumgaRepository<T> {
	
	@Autowired
	private SessionFactory sessionFactory;
	private Class<T> clazz;
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Transactional(propagation = Propagation.MANDATORY)
	public T saveOrUpdate(T model) {
		session().saveOrUpdate(model);
		return model;
	}
	
	@Transactional(propagation = Propagation.MANDATORY)
	public void delete(T model) {
		session().delete(model);
	}
	
	@Transactional(readOnly = true)
	public T load(Long id) {
		T entity = clazz().cast(session().get(clazz(), id));
		
		if (entity == null) 
			throw new EntityNotFoundException(clazz().getSimpleName() + " with id " + id);
		
		return entity;
	}
	
	@SuppressWarnings("unchecked")
	public Class<T> clazz() {
		if (clazz == null) 
			clazz = (Class<T>) ReflectionUtils.inferGenericType(getClass());
		
		return clazz;
	}
	
	private Session session() {
		return sessionFactory.getCurrentSession();
	}
		
	public void setClazz(Class<T> clazz) {
		this.clazz = clazz;
	}
	
	public void flush() {
		session().flush();
	}
	
	public void clear() {
		session().clear();
	}
	
}
