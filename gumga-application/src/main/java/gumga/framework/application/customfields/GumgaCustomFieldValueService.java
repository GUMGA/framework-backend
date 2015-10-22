package gumga.framework.application.customfields;

import gumga.framework.application.GumgaService;
import gumga.framework.domain.GumgaModel;
import gumga.framework.domain.customfields.GumgaCustomField;
import gumga.framework.domain.customfields.GumgaCustomFieldValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GumgaCustomFieldValueService extends GumgaService<GumgaCustomFieldValue, Long> {

    private final GumgaCustomFieldValueRepository repository;

    @Autowired
    public GumgaCustomFieldValueService(GumgaCustomFieldValueRepository repository) {
        super(repository);
        this.repository = repository;
    }

    Object getValue(GumgaCustomField cf, GumgaModel obj) {
        return repository.findByFieldAndGumgaModelId(cf, (Long) obj.getId());
    }

    @Override
    public GumgaCustomFieldValue save(GumgaCustomFieldValue newValue) {
        GumgaCustomFieldValue oldValue = repository.findByFieldAndGumgaModelId(newValue.getField(), newValue.getGumgaModelId());
        if (oldValue==null){
            System.out.println("GRAVANDO NOVO "+newValue);
            return super.save(newValue); 
        }
        oldValue.setValue(newValue.getValue());
        System.out.println("ALTERANDO VELHO "+oldValue);
        return super.save(oldValue); 
    }
    
    
}
