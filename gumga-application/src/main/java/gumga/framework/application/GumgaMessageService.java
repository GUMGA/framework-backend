package gumga.framework.application;

import gumga.framework.domain.GumgaMessage;
import java.io.Serializable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GumgaMessageService extends GumgaService<GumgaMessage, Long> implements Serializable {

    private GumgaMessageRepository repository;

    @Autowired
    public GumgaMessageService(GumgaMessageRepository repository) {
        super(repository);
        this.repository = repository;
    }

}
