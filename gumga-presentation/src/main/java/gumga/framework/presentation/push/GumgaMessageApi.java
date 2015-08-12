package gumga.framework.presentation.push;

import gumga.framework.application.GumgaService;
import gumga.framework.domain.GumgaMessage;
import gumga.framework.presentation.GumgaAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/gumgamessage")
public class GumgaMessageApi extends GumgaAPI<GumgaMessage, Long> {

    @Autowired
    public GumgaMessageApi(GumgaService<GumgaMessage, Long> service) {
        super(service);
    }

}
