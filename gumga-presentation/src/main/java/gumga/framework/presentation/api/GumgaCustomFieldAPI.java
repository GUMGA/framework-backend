package gumga.framework.presentation.api;

import gumga.framework.application.GumgaService;
import gumga.framework.domain.customfields.GumgaCustomField;
import gumga.framework.presentation.GumgaAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/gumgacustomfield")
public class GumgaCustomFieldAPI extends GumgaAPI<GumgaCustomField, Long> {

    @Autowired
    public GumgaCustomFieldAPI(GumgaService<GumgaCustomField, Long> service) {
        super(service);
    }


}

