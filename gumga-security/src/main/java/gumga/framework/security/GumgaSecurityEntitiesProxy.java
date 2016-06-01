package gumga.framework.security;


import com.wordnik.swagger.annotations.ApiOperation;
import gumga.framework.core.GumgaThreadScope;
import gumga.framework.core.GumgaValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/security")
public class GumgaSecurityEntitiesProxy {

    private final RestTemplate restTemplate;
    @Autowired
    private GumgaValues gumgaValues;

    public GumgaSecurityEntitiesProxy() {
        this.restTemplate = new RestTemplate();
    }

    @ApiOperation(value = "getAllOrganizations", notes = "Buscar todas as organizações.")
    @RequestMapping(method = RequestMethod.GET, value = "/organizations")
    public Map getAllOrganizations() {
        final String param = "?gumgaToken=" + GumgaThreadScope.gumgaToken.get() + "&pageSize=" + (Integer.MAX_VALUE - 1);
        final String url = gumgaValues.getGumgaSecurityUrl().replace("/publicoperations", "/api/organization") + param;
        Map  response = restTemplate.getForObject(url, Map.class);
        return response;
    }
}
