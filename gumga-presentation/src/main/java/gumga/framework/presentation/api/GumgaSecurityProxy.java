/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gumga.framework.presentation.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 *
 * @author munif
 */
@RestController
@RequestMapping("/public/token")
public class GumgaSecurityProxy {

    private final RestTemplate restTemplate;
    private Environment environment;

    //TODO pensar em uma classe gumga constants para expor public .. com todos os end points
    public final static String GUMGASECURITY_URL = "gumga.security.url";
    public final static String GUMGASECURITY_CREATE_ENDPOINT = "/public/token/create";

    @Autowired
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public GumgaSecurityProxy() {
        restTemplate = new RestTemplate();
    }

    @RequestMapping("/create/{user}/{password}")
    public Map create(@PathVariable String user, @PathVariable String password) {

        String secUrl = environment.getProperty(GUMGASECURITY_URL);
        String url = GUMGASECURITY_CREATE_ENDPOINT + "/" + user + "/" + password;
        Map resposta = restTemplate.getForObject(url, Map.class);
        return resposta;
    }

}
