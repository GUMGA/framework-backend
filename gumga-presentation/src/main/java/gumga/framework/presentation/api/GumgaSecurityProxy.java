/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gumga.framework.presentation.api;

import java.util.Map;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author munif
 */
@RestController
@RequestMapping("/public/token")
public class GumgaSecurityProxy {

    private final RestTemplate restTemplate;
    private final String GUMGASECURITY_CREATE_ENDPOINT = "http://localhost:8084/gumgasecurity-presentation/public/token/create";

    public GumgaSecurityProxy() {
        restTemplate = new RestTemplate();
    }

    @RequestMapping("/create/{user}/{password}")
    public Map create(@PathVariable String user, @PathVariable String password) {

        String url = GUMGASECURITY_CREATE_ENDPOINT + "/" + user + "/" + password;
        Map resposta = restTemplate.getForObject(url, Map.class);
        return resposta;
    }

}
