package gumga.framework.presentation;

import com.wordnik.swagger.annotations.ApiOperation;
import gumga.framework.domain.GumgaServiceable;
import gumga.framework.presentation.api.AbstractNoDeleteGumgaAPI;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public abstract class AbstractGumgaAPI<T> extends AbstractNoDeleteGumgaAPI<T> {

    protected GumgaServiceable<T> service;

    public AbstractGumgaAPI(GumgaServiceable<T> service) {
        super(service);
        this.service = service;
    }

    @Transactional
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "delete", notes = "Deleta objeto com o id correspondente.")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public RestResponse<T> delete(@PathVariable Long id) {
        T entity = service.view(id);
        service.delete(entity);
        return new RestResponse<T>(getEntityDeletedMessage(entity));
    }

    public void setService(GumgaServiceable<T> service) {
        this.service = service;
        super.setService(service);
    }

}
