package gumga.framework.application.service;

import gumga.framework.application.customfields.GumgaCustomEnhancerService;
import gumga.framework.core.exception.NoMultiTenancyException;
import gumga.framework.core.utils.ReflectionUtils;
import gumga.framework.domain.GumgaMultitenancy;
import gumga.framework.domain.customfields.GumgaCustomizableModel;
import gumga.framework.domain.domains.GumgaOi;
import gumga.framework.domain.repository.GumgaCrudRepository;
import gumga.framework.domain.repository.GumgaMultitenancyUtil;
import java.io.Serializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractGumgaService<T, ID extends Serializable> {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected final GumgaCrudRepository<T, ID> repository;

    @Autowired
    protected GumgaCustomEnhancerService gces;

    public AbstractGumgaService(GumgaCrudRepository<T, ID> repository) {
        this.repository = repository;
    }

    @SuppressWarnings("unchecked")
    public Class<T> clazz() {
        return (Class<T>) ReflectionUtils.inferGenericType(getClass());
    }

    public void loadGumgaCustomFields(Object entity) {
        if (entity instanceof GumgaCustomizableModel) {
            gces.loadCustomFields((GumgaCustomizableModel) entity);
        }
    }

    public GumgaOi gumgaOiForSearch() {
        try {
            String oiPattern = GumgaMultitenancyUtil.getMultitenancyPattern(clazz().getAnnotation(GumgaMultitenancy.class));
            GumgaOi oi = new GumgaOi(oiPattern);
            return oi;
        }
        catch(Exception ex){
            throw new NoMultiTenancyException("The class "+clazz().getCanonicalName()+" haven't MultiTenancy" );
        }
    }
    public GumgaOi gumgaOiForSearchWithWildCard() {
        try {
            String oiPattern = GumgaMultitenancyUtil.getMultitenancyPattern(clazz().getAnnotation(GumgaMultitenancy.class));
            GumgaOi oi = new GumgaOi(oiPattern.concat("%"));
            return oi;
        }
        catch(Exception ex){
            throw new NoMultiTenancyException("The class "+clazz().getCanonicalName()+" haven't MultiTenancy" );
        }
    }

}
