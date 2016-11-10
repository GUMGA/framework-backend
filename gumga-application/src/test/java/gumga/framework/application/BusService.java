package gumga.framework.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BusService extends GumgaService<Bus, Long> {

    @Autowired
    public BusService(BusRepository repository) {
        super(repository);
    }

}
