package gumga.framework.presentation;

import gumga.framework.core.GumgaIdable;
import gumga.framework.core.QueryObject;
import gumga.framework.core.SearchResult;
import gumga.framework.domain.GumgaService;
import gumga.framework.validation.exception.InvalidEntityException;

import java.lang.reflect.Constructor;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

public abstract class GumgaDTOAPI<T> {
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	private final GumgaGateway<T> gateway;
	
	protected GumgaDTOAPI(GumgaGateway<T> gateway) {
		this.gateway = gateway;
	}
	
	public void beforeSearch(QueryObject query) { }
	public void afterSearch(QueryObject query) { }
	
	@SuppressWarnings("unchecked")
	@RequestMapping
	public SearchResult<T> pesquisa(QueryObject query) {
		beforeSearch(query);
		SearchResult<?> pesquisa = getService().pesquisa(query);
		afterSearch(query);
		 
		return new SearchResult<T>(query, pesquisa.getCount(), getTranslator().from((List<GumgaIdable>) pesquisa.getValues()));
	}
	
	public void beforeCreate(T entity) { }
	public void afterCreate(T entity) { }

	@Transactional
	@RequestMapping(method = RequestMethod.POST)
	public RestResponse<T> save(@RequestBody @Valid T model, BindingResult result) {
		beforeCreate(model);
		T entity = saveOrCry(model, result);
		afterCreate(entity);
		
		return new RestResponse<T>(entity, getEntitySavedMessage(entity));
	}
	
	public void beforeLoad() { 	}
	public void afterLoad(T entity) { }

	@RequestMapping("/{id}")
	public T load(@PathVariable Long id) {
		beforeLoad();
		T entity = (T) getTranslator().from(getService().view(id));
		afterLoad(entity);
		
		return entity;
	}
	
	public void beforeUpdate(T entity) { }
	public void afterUpdate(T entity) { }

	@Transactional
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = "application/json")
	public RestResponse<T> update(@PathVariable("id") Long id, @RequestBody T model, BindingResult result) {
		beforeUpdate(model);
		T entity = saveOrCry(model, result);
		afterUpdate(entity);
		
		return new RestResponse<T>(entity, getEntityUpdateMessage(entity));
	}
	
	public void beforeDelete(T entity) { }
	public void afterDelete() { }

	@Transactional
	@ResponseStatus(value = HttpStatus.OK)
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public RestResponse<T> delete(@PathVariable Long id, HttpServletRequest request) {
		GumgaIdable entity = getService().view(id);
		
		beforeDelete(getTranslator().from(entity));
		getService().delete(entity);
		afterDelete();
		
		return new RestResponse<T>("Resource deleted successfully");
	}

	@RequestMapping("/new")
	public T initialState() {
		return initialValue();
	}

	protected T initialValue() {
		try {
			@SuppressWarnings("unchecked")
			Constructor<T> constructor = (Constructor<T>) getTranslator().dtoClass().getDeclaredConstructors()[0];
			constructor.setAccessible(true);
			return constructor.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	private <X extends GumgaIdable> T saveOrCry(T model, BindingResult result) {
		if (result.hasErrors())
			throw new InvalidEntityException(result);

		return getTranslator().from(getService().save((X) getTranslator().to(model)));
	}
		
	@SuppressWarnings("unchecked")
	private <X extends GumgaIdable> GumgaService<X> getService() {
		return (GumgaService<X>) gateway.service();
	}
	
	@SuppressWarnings("unchecked")
	private <X extends GumgaIdable> GumgaTranslator<X, T> getTranslator() {
		return (GumgaTranslator<X, T>) gateway.translator();
	}
	
	protected String getEntityName(T entity) {
		return entity.getClass().getSimpleName();
	}
	
	protected String getEntitySavedMessage(T entity) {
		return getEntityName(entity) + " saved successfully";
	}
	
	protected String getEntityUpdateMessage(T entity) {
		return getEntitySavedMessage(entity);
	}
	
	protected String getEntityDeletedMessage(T entity) {
		return getEntityName(entity) + " deleted successfully";
	}

}
