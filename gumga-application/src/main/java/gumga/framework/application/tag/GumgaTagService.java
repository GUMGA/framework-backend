package gumga.framework.application.tag;

import gumga.framework.application.GumgaService;
import gumga.framework.core.GumgaIdable;
import gumga.framework.core.QueryObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import org.hibernate.Hibernate;

import gumga.framework.domain.tag.GumgaTag;
import gumga.framework.domain.tag.GumgaTagDefinition;
import java.util.List;

@Service
public class GumgaTagService extends GumgaService<GumgaTag, Long> {

    private GumgaTagRepository repository;

    @Autowired
    public GumgaTagService(GumgaTagRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Transactional
    public GumgaTag loadGumgaTagFat(Long id) {
        GumgaTag obj = repository.findOne(id);
        Hibernate.initialize(obj.getValues());
        return obj;
    }

    public GumgaTag createNew(GumgaTagDefinition tagDef, String objectType, Long objectId) {
        GumgaTag tag = new GumgaTag(tagDef);
        tag.setObjectType(objectType);
        tag.setObjectId(objectId);
        return tag;
    }

    @Transactional
    public List<GumgaTag> findByObjectTypeAndObjectId(String objectType, Long objectId) {
        QueryObject qo = new QueryObject();
        qo.setAq("obj.objectType='" + objectType + "' AND obj.objectId=" + objectId);
        return repository.search(qo).getValues();
    }
    
    @Transactional
    public List<GumgaTag> findByObject(GumgaIdable<Long> obj) {
        return findByObjectTypeAndObjectId(obj.getClass().getName(), obj.getId());
    }

}
