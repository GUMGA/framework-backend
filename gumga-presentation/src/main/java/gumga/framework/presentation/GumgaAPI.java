package gumga.framework.presentation;


import gumga.framework.core.QueryObject;
import gumga.framework.core.SearchResult;
import gumga.framework.domain.GumgaIdable;
import gumga.framework.domain.GumgaService;
import gumga.framework.domain.exception.InvalidEntityException;

import java.lang.reflect.Constructor;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public abstract class GumgaAPI<T extends GumgaIdable> {

	@Autowired
	protected GumgaService<T> service;
	protected Logger logger = Logger.getLogger(getClass().getSimpleName());
	
	public void beforeSearch(QueryObject query) { }
	public void afterSearch(QueryObject query) { }

	@RequestMapping
	public SearchResult<T> pesquisa(QueryObject query) {
		beforeSearch(query);
		SearchResult<T> pesquisa = service.pesquisa(query);
		afterSearch(query);
		 
		return pesquisa;
	}
	
	public void beforeCreate(T entity) { }
	public void afterCreate(T entity) { }

	@Transactional
	@RequestMapping(method = RequestMethod.POST)
	public T save(@RequestBody @Valid T model, BindingResult result) {
		beforeCreate(model);
		T entity = saveOrCry(model, result);
		afterCreate(entity);
		
		return entity;
	}
	
	public void beforeLoad() { 	}
	public void afterLoad(T entity) { }

	@RequestMapping("/{id}")
	public T carrega(@PathVariable Long id) {
		beforeLoad();
		T entity = service.view(id);
		afterLoad(entity);
		
		return entity;
	}
	
	public void beforeUpdate(T entity) { }
	public void afterUpdate(T entity) { }

	@Transactional
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = "application/json")
	public T update(@PathVariable("id") Long id, @RequestBody T model, BindingResult result) {
		beforeUpdate(model);
		T entity = saveOrCry(model, result);
		afterUpdate(entity);
		
		return entity;
	}
	
	public void beforeDelete(T entity) { }
	public void afterDelete() { }

	@Transactional
	@ResponseStatus(value = HttpStatus.OK)
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable Long id, HttpServletRequest request) {
		T entity = service.view(id);
		
		beforeDelete(entity);
		service.delete(entity);
		afterDelete();
	}

	@RequestMapping("/new")
	public T initialState() {
		return initialValue();
	}

	protected T initialValue() {
		try {
			Constructor<T> constructor = service.clazz().getDeclaredConstructor();
			constructor.setAccessible(true);
			return constructor.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private T saveOrCry(T model, BindingResult result) {
		if (result.hasErrors())
			throw new InvalidEntityException(result);

		return service.save(model);
	}

}
