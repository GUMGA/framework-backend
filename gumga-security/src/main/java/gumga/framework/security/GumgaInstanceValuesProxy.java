/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gumga.framework.security;

import com.wordnik.swagger.annotations.ApiOperation;
import gumga.framework.core.GumgaThreadScope;
import gumga.framework.core.GumgaValues;
import gumga.framework.presentation.RestResponse;
import java.util.Collections;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author munif
 */
@RestController
@RequestMapping("/api/proxy/instancevalues")
public class GumgaInstanceValuesProxy {

    @Autowired
    private GumgaValues gumgaValues;

    private String getBaseUrl() {
        return gumgaValues.getGumgaSecurityUrl();
    }

    private RestTemplate restTemplate;

    private RestTemplate getRestTemplate() {
        if (restTemplate == null) {
            restTemplate = new RestTemplate();
        }
        return restTemplate;
    }

    @Transactional
    @ApiOperation(value = "List", notes = "Carrega entidade pela chave informada.")
    @RequestMapping(method = RequestMethod.GET)
    public Map load() {
        String software = GumgaThreadScope.softwareName.get();
        String token = GumgaThreadScope.gumgaToken.get();
        String url = getBaseUrl() + "/token/instance-values/" + software + "/" + token ;
        
        Map response = getRestTemplate().getForObject(url, Map.class);
        if (response == null) {
            response = Collections.EMPTY_MAP;
        }
        return response;
    }

}
