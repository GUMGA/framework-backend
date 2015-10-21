package gumga.framework.application.customfields;

import gumga.framework.application.GumgaService;
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
}
