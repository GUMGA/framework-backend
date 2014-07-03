package gumga.framework.application;

import gumga.framework.application.repository.GumgaAbstractRepository;
import gumga.framework.application.repository.GumgaNoDeleteRepository;
import gumga.framework.core.GumgaIdable;
import gumga.framework.domain.GumgaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Scope("prototype")
public class GumgaGenericRepository<T extends GumgaIdable<?>> extends GumgaAbstractRepository<T> implements GumgaRepository<T> {
	
	@Autowired
	private GumgaNoDeleteRepository<T> delegate;
	
	@Transactional(propagation = Propagation.MANDATORY)
	public void delete(T model) {
		session().delete(model);
	}

	public T load(Long id) {
		return delegate.load(id);
	}

	@Transactional(propagation = Propagation.MANDATORY)
	public T saveOrUpdate(T model) {
		return delegate.saveOrUpdate(model);
	}

	public void flush() {
		delegate.flush();
	}

	public void clear() {
		delegate.clear();
	}
	
}
