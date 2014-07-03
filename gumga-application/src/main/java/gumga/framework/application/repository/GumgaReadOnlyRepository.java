package gumga.framework.application.repository;

import javax.persistence.EntityNotFoundException;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import gumga.framework.core.GumgaIdable;
import gumga.framework.domain.repository.GumgaReadableRepository;

@Repository
@Scope("prototype")
public class GumgaReadOnlyRepository<T extends GumgaIdable<?>> extends GumgaAbstractRepository<T> implements GumgaReadableRepository<T> {

	@Transactional(readOnly = true)
	public T load(Long id) {
		T entity = clazz().cast(session().get(clazz(), id));
		
		if (entity == null) 
			throw new EntityNotFoundException(clazz().getSimpleName() + " with id " + id);
		
		return entity;
	}
	
}
