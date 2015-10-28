package gumga.framework.presentation.api;

import gumga.framework.application.customfields.GumgaCustomEnhancerService;
import gumga.framework.core.utils.ReflectionUtils;
import gumga.framework.presentation.validation.Error;
import gumga.framework.presentation.validation.ErrorResource;
import gumga.framework.presentation.validation.FieldErrorResource;

import java.lang.reflect.Constructor;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public abstract class AbstractProtoGumgaAPI<T> {

    @Autowired
    private Validator validator;
    
    @Autowired
    protected GumgaCustomEnhancerService gces;

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    
    @RequestMapping(value="/new",method = RequestMethod.GET)
    public T initialState() {
        T entity=initialValue();
        gces.setDefaultValues(entity);
        return entity;
    }

    protected T initialValue() {
        try {
            Constructor<T> constructor = clazz().getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

    @RequestMapping(value = "/validate", method = RequestMethod.POST)
    public ErrorResource validate(@RequestBody T entity) {
        try {
            Set<ConstraintViolation<T>> errors = validator.validate(entity);
            if (errors.isEmpty()) {
                return ErrorResource.NO_ERRORS;
            }

            ErrorResource invalidEntity = new ErrorResource(Error.INVALID_ENTITY, "Invalid Entity");
            invalidEntity.setData(entity);
            invalidEntity.setDetails("Invalid Entity State");

            for (ConstraintViolation<T> violation : errors) {
                FieldErrorResource fieldErrorResource = new FieldErrorResource();
                fieldErrorResource.setResource(violation.getRootBeanClass().getCanonicalName());
                fieldErrorResource.setField(violation.getPropertyPath().toString());
                fieldErrorResource.setCode(violation.getMessageTemplate());
                fieldErrorResource.setMessage(violation.getMessage());

                invalidEntity.addFieldError(fieldErrorResource);
            }

            return invalidEntity;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public Class<T> clazz() {
        return (Class<T>) ReflectionUtils.inferGenericType(getClass());
    }

}
