package gumga.framework.presentation.api;

import com.wordnik.swagger.annotations.ApiOperation;
import gumga.framework.annotations.GumgaSwagger;
import gumga.framework.domain.GumgaServiceable;
import gumga.framework.domain.service.GumgaWritableServiceable;
import gumga.framework.domain.tag.GumgaTag;
import gumga.framework.presentation.RestResponse;
import gumga.framework.validation.exception.InvalidEntityException;
import java.util.List;
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

    @GumgaSwagger
    @Transactional
    @ApiOperation(value = "save", notes = "Salva o objeto correspodente.")
    @RequestMapping(method = RequestMethod.POST)
    public RestResponse<T> save(@RequestBody @Valid T model, BindingResult result) {
        beforeSave(model);
        T entity = saveOrCry(model, result);
        return new RestResponse<T>(entity, getEntitySavedMessage(entity));
    }

    @GumgaSwagger
    @Transactional
    @ApiOperation(value = "update", notes = "Atualiza o objeto pelo id correspondente.")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = "application/json")
    public RestResponse<T> update(@PathVariable("id") Long id,
            @Valid @RequestBody T model, BindingResult result) {
        beforeUpdate(id, model);
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

    protected void beforeSave(T model) {

    }

    protected void beforeUpdate(Long id, T model) {

    }

    @Transactional
    @RequestMapping(value = "tags", method = RequestMethod.POST)
    public void saveAll(@RequestBody TagsTo tags) {
//        System.out.println("-------------------------------> Fez uma chamada com "+tags.tags.size()+" tags ");

        for (GumgaTag tag : tags.tags) {
            tag.setObjectType(clazz().getCanonicalName());
//            System.out.println("---------------ID  ---->" + tag.getId());
//            System.out.println("---------------TYPE---->" + tag.getObjectType());
//            System.out.println("-----------TAG------  -->" + tag.getDefinition().getName());
//            System.out.println("-----------DEFINITION-->" + tag.getDefinition());
//            System.out.println("-----------OBJECT ID--->" + tag.getObjectId());
//            System.out.println("-----------VALUES  ---->" + tag.getValues());
//            System.out.println("--------------------------");
        }
        tags.tags.stream().forEach(t -> gts.save(t));
    }

}

class TagsTo {

    public List<GumgaTag> tags;

    public List<GumgaTag> getTags() {
        return tags;
    }

    public void setTags(List<GumgaTag> tags) {
        this.tags = tags;
    }

}
