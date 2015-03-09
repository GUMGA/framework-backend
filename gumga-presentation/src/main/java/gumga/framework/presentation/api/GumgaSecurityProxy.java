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
    @Autowired
    private Environment environment;
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public GumgaSecurityProxy() {
        restTemplate = new RestTemplate();
    }

    @RequestMapping("/create/{user}/{password}")
    public Map create(@PathVariable String user, @PathVariable String password) {

        String secUrl = environment.getProperty("gumga.security.url", "http://localhost:8084/gumgasecurity-presentation");
        String url = secUrl + "/public/token/create/" + user + "/" + password;
        Map resposta = restTemplate.getForObject(url, Map.class);
        return resposta;
    }

}
