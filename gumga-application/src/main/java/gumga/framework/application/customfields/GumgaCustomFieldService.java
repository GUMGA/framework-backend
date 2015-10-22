package gumga.framework.application.customfields;

import gumga.framework.application.GumgaService;
import gumga.framework.domain.customfields.GumgaCustomField;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GumgaCustomFieldService extends GumgaService<GumgaCustomField, Long> {

    private final GumgaCustomFieldRepository repository;

    @Autowired
    public GumgaCustomFieldService(GumgaCustomFieldRepository repository) {
        super(repository);
        this.repository = repository;
    }

    List<GumgaCustomField> findByClass(String c) {
        return repository.findByClazz(c);
    }

    List<GumgaCustomField> findByClass(Class c) {
        return repository.findByClazz(c.getName());
    }

    List<GumgaCustomField> findByClass(Object obj) {
        return repository.findByClazz(obj.getClass().getName());
    }

}
