package gumga.framework.application.service;

import gumga.framework.core.GumgaIdable;
import gumga.framework.core.utils.ReflectionUtils;
import gumga.framework.domain.GumgaFinder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractGumgaService<T extends GumgaIdable<?>> {
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	protected GumgaFinder<T> finder;
	
	@Autowired
	public void setFinder(GumgaFinder<T> finder) {
		this.finder = finder;
	}
	
	@SuppressWarnings("unchecked")
	public Class<T> clazz() {
		return (Class<T>) ReflectionUtils.inferGenericType(getClass());
	}

}
