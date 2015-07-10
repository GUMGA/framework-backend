package gumga.framework.presentation.api;

import gumga.framework.application.GumgaService;
import gumga.framework.application.GumgaUserDataService;
import gumga.framework.core.SearchResult;
import gumga.framework.domain.GumgaUserData;
import gumga.framework.presentation.GumgaAPI;
import java.io.Serializable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/gumgauserdata")
public class GumgaUserDataApi extends GumgaAPI<GumgaUserData, Long> {

    @Autowired

    public GumgaUserDataApi(GumgaService<GumgaUserData, Long> service) {
        super(service);
    }

    @RequestMapping("keyprefix/{prefix}")
    public SearchResult<GumgaUserData> queryByKeyPrefix(@PathVariable String prefix) {
        return ((GumgaUserDataService) service).searchByKeyPrefix(prefix);

    }

}
