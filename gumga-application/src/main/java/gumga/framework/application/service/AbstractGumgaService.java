package gumga.framework.application.service;

import gumga.framework.core.utils.ReflectionUtils;
import gumga.framework.domain.repository.GumgaCrudRepository;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractGumgaService<T, ID extends Serializable> {
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	protected final GumgaCrudRepository<T, ID> repository;
	
	public AbstractGumgaService(GumgaCrudRepository<T, ID> repository) {
		this.repository = repository;
	}

	@SuppressWarnings("unchecked")
	public Class<T> clazz() {
		return (Class<T>) ReflectionUtils.inferGenericType(getClass());
	}

}
