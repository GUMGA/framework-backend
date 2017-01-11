package gumga.framework.application.service;

import gumga.framework.core.GumgaIdable;
import gumga.framework.core.QueryObject;
import gumga.framework.core.SearchResult;
import gumga.framework.domain.repository.GumgaCrudRepository;
import gumga.framework.domain.service.GumgaReadableServiceable;
import gumga.framework.domain.service.GumgaWritableServiceable;
import java.io.Serializable;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Scope("prototype")
public abstract class GumgaNoDeleteService<T extends GumgaIdable<?>, ID extends Serializable> extends AbstractGumgaService<T, ID> implements GumgaReadableServiceable<T>, GumgaWritableServiceable<T> {

	public GumgaNoDeleteService(GumgaCrudRepository<T, ID> repository) {
		super(repository);
	}

	/**
	 * Processo executado antes do método Pesquisa da classe {@link GumgaNoDeleteService}
	 * @param query
	 */
	public void beforePesquisa(QueryObject query) { }

	/**
	 * Processo executado apos do método Pesquisa da classe {@link GumgaNoDeleteService}
	 * @param result
	 */
	public void afterPesquisa(SearchResult<T> result) { }
	
	@Transactional(readOnly = true)
	public SearchResult<T> pesquisa(QueryObject query) {
		beforePesquisa(query);
		SearchResult<T> result = repository.search(query);
		afterPesquisa(result);
		
		return result;
	}

	/**
	 * Processo executado antes do método view da classe {@link GumgaNoDeleteService}
	 * @param id
	 */
	public void beforeView(ID id) {}

	/**
	 * Processo executado apos do método view da classe {@link GumgaNoDeleteService}
	 * @param entity
	 */
	public void afterView(T entity) {}

	/**
	 * Pesquisa a entidade tipada na classe {@link GumgaNoDeleteService} pela primary key
	 * @param id
	 * @return
	 */
	@Transactional(readOnly = true)
	public T view(ID id) {
		beforeView(id);
		T entity = repository.findOne(id);
                loadGumgaCustomFields(entity);
		afterView(entity);
		
		return entity;
	}

	/**
	 * Processo executado antes do método save e update da classe {@link GumgaNoDeleteService}
	 * @param entity
	 */
	private void beforeSaveOrUpdate(T entity) {
		if (entity.getId() == null)
			beforeSave(entity);
		else
			beforeUpdate(entity);
	}

	/**
	 * Processo executado apos do método save e update da classe {@link GumgaNoDeleteService}
	 * @param entity
	 */
	private void afterSaveOrUpdate(T entity) {
		if (entity.getId() == null)
			afterSave(entity);
		else
			afterUpdate(entity);
	}

	/**
	 * Processo executado antes do método save da classe {@link GumgaNoDeleteService}
	 * @param entity
	 */
	public void beforeSave(T entity) {}

	/**
	 * Processo executado antes do método update da classe {@link GumgaNoDeleteService}
	 * @param entity
	 */
	public void beforeUpdate(T entity) {}

	/**
	 * Processo executado apos do método save da classe {@link GumgaNoDeleteService}
	 * @param entity
	 */
	public void afterSave(T entity) {}

	/**
	 * Processo executado apos do método update da classe {@link GumgaNoDeleteService}
	 * @param entity
	 */
	public void afterUpdate(T entity) {}

	/**
	 * Salvar entidade na base de dados
	 * @param resource
	 * @return
	 */
	@Transactional
	public T save(T resource) {
		beforeSaveOrUpdate(resource);
		T entity = repository.save(resource);
                gces.saveCustomFields(resource);
		afterSaveOrUpdate(entity);
		
		return entity;
	}

	/**
	 * Sincronizar os dados do {@link javax.persistence.EntityManager} com o banco de dados.
	 */
	public void forceFlush() {
		repository.flush();
	}
	
}
