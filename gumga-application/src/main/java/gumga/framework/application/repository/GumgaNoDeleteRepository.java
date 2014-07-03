package gumga.framework.application.repository;

import gumga.framework.core.GumgaIdable;
import gumga.framework.domain.repository.GumgaReadableRepository;
import gumga.framework.domain.repository.GumgaWritableRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Scope("prototype")
public class GumgaNoDeleteRepository<T extends GumgaIdable<?>> extends GumgaAbstractRepository<T> implements GumgaReadableRepository<T>, GumgaWritableRepository<T> {
	
	@Autowired
	private GumgaReadOnlyRepository<T> readOnlyRepository;

	@Transactional(propagation = Propagation.MANDATORY)
	public T saveOrUpdate(T model) {
		session().saveOrUpdate(model);
		return model;
	}

	public void flush() {
		session().flush();
	}
	
	public void clear() {
		session().clear();
	}

	public T load(Long id) {
		return readOnlyRepository.load(id);
	}

}
