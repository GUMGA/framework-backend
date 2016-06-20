package gumga.framework.application.tag;

import gumga.framework.application.GumgaService;
import gumga.framework.application.QueryObjectLikeDecorator;
import gumga.framework.core.QueryObject;
import gumga.framework.core.SearchResult;
import gumga.framework.domain.tag.GumgaTagDefinition;
import javax.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GumgaTagDefinitionService extends GumgaService<GumgaTagDefinition, Long> {

    private GumgaTagDefinitionRepository repository;

    @Autowired
    public GumgaTagDefinitionService(GumgaTagDefinitionRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Transactional
    public GumgaTagDefinition loadGumgaTagDefinitionFat(Long id) {
        GumgaTagDefinition obj = repository.findOne(id);
        Hibernate.initialize(obj.getAttributes());
        return obj;
    }
    
    public GumgaTagDefinition createNew(String name){
        return new GumgaTagDefinition(name);
    }

    public GumgaTagDefinition createNew(String name, String... attributes){
        GumgaTagDefinition def = new GumgaTagDefinition(name);
        def.addAttributes(attributes);
        return def;
    }
    
    
    @Override
    public SearchResult<GumgaTagDefinition> pesquisa(QueryObject query) {
        return repository.search(new QueryObjectLikeDecorator(query).build());
    }
    
}
