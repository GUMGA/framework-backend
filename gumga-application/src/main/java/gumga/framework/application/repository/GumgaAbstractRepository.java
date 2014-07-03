package gumga.framework.application.repository;

import gumga.framework.application.GumgaSessionStrategy;
import gumga.framework.core.GumgaIdable;
import gumga.framework.core.utils.ReflectionUtils;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class GumgaAbstractRepository<T extends GumgaIdable<?>> {
	
	@Autowired
	private GumgaSessionStrategy sessionStrategy;
	
	private Class<T> clazz;
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@SuppressWarnings("unchecked")
	public Class<T> clazz() {
		if (clazz == null) 
			clazz = (Class<T>) ReflectionUtils.inferGenericType(getClass());
		
		return clazz;
	}
	
	protected Session session() {
		return sessionStrategy.getSession();
	}
		
	public void setClazz(Class<T> clazz) {
		this.clazz = clazz;
	}
	
}
