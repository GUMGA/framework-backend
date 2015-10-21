package gumga.framework.application.customfields;

import gumga.framework.application.GumgaService;
import gumga.framework.domain.customfields.GumgaCustomField;
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
}
