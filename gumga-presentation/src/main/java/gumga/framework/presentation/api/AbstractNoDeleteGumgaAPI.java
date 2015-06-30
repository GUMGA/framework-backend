package gumga.framework.presentation.api;

import gumga.framework.domain.GumgaServiceable;
import gumga.framework.domain.service.GumgaWritableServiceable;
import gumga.framework.presentation.RestResponse;
import gumga.framework.validation.exception.InvalidEntityException;

import javax.validation.Valid;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public abstract class AbstractNoDeleteGumgaAPI<T> extends
        AbstractReadOnlyGumgaAPI<T> {

    protected GumgaWritableServiceable<T> service;

    public AbstractNoDeleteGumgaAPI(GumgaWritableServiceable<T> service) {
        super(service);
        this.service = service;
    }

    @Transactional
    @RequestMapping(method = RequestMethod.POST)
    public RestResponse<T> save(@RequestBody @Valid T model,
            BindingResult result) {
        T entity = saveOrCry(model, result);
        return new RestResponse<T>(entity, getEntitySavedMessage(entity));
    }

    @Transactional
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = "application/json")
    public RestResponse<T> update(@PathVariable("id") Long id,
            @Valid @RequestBody T model, BindingResult result) {
        T entity = saveOrCry(model, result);
        return new RestResponse<T>(entity, getEntityUpdateMessage(entity));
    }

    private T saveOrCry(T model, BindingResult result) {
        if (result.hasErrors()) {
            throw new InvalidEntityException(result);
        }

        return service.save(model);
    }

    @Override
    public void setService(GumgaServiceable<T> service) {
        this.service = service;
        super.setService(service);
    }

}
