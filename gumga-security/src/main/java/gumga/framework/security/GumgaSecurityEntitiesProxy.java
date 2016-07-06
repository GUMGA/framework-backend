package gumga.framework.security;


import com.wordnik.swagger.annotations.ApiOperation;
import gumga.framework.core.GumgaThreadScope;
import gumga.framework.core.GumgaValues;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

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
        Map  response = new HashedMap();
        try {
            response = restTemplate.getForObject(url, Map.class);
            return response;
        } catch (Exception e) {
            response.put(403, "Acesso negado!" +  e);
        }

        return response;
    }
}
