package gumga.framework.application;

import gumga.framework.domain.GumgaMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GumgaMessageService extends GumgaService<GumgaMessage, Long> {

    private GumgaMessageRepository repository;

    @Autowired
    public GumgaMessageService(GumgaMessageRepository repository) {
        super(repository);
        this.repository = repository;
    }

}
