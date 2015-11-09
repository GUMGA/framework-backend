package gumga.framework.application.customfields;

import gumga.framework.application.GumgaService;
import gumga.framework.core.GumgaThreadScope;
import gumga.framework.core.QueryObject;
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

    List<GumgaCustomField> findByClass(String clazzName) {
        return repository.search(getQueryObject(clazzName)).getValues();

    }

    List<GumgaCustomField> findByClass(Class c) {
        return findByClass(c.getName());
    }

    List<GumgaCustomField> findByClass(Object obj) {
        return findByClass(obj.getClass());
    }

    private QueryObject getQueryObject(String clazzName) {
        QueryObject qo = new QueryObject();
        qo.setAq("obj.clazz='" + clazzName + "'");
        qo.setSortField("visualizationOrder");
        return qo;

    }

}
