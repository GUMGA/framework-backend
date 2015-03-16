/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gumga.framework.security;

import gumga.framework.core.GumgaValues;
import gumga.framework.core.UserAndPassword;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;
import java.util.Set;
import javax.transaction.Transactional;

/**
 *
 * @author munif
 */
@RestController
@RequestMapping("/public/token")
class GumgaSecurityProxy {

    private final RestTemplate restTemplate;
    @Autowired
    private GumgaValues gumgaValues;

    public GumgaSecurityProxy() {
        restTemplate = new RestTemplate();
    }

    @RequestMapping("/create/{user}/{password}")
    public Map create(@PathVariable String user, @PathVariable String password) {
        String url = gumgaValues.getGumgaSecurityUrl() + "/public/token/create/" + user + "/" + password;
        Map resposta = restTemplate.getForObject(url, Map.class);
        return resposta;
    }

    @RequestMapping(value = "/{token}", method = RequestMethod.DELETE)
    public Map delete(@PathVariable String token) {
        String url = gumgaValues.getGumgaSecurityUrl() + "/public/token/" + token;
        restTemplate.delete(url);
        return GumgaSecurityCode.OK.response();
    }

    @RequestMapping(method = RequestMethod.POST)
    public Map login(@RequestBody UserAndPassword login) {
        String url = gumgaValues.getGumgaSecurityUrl() + "/public/token";
        Map resposta = restTemplate.postForObject(url, login, Map.class);
        return resposta;
    }

    @RequestMapping(value = "/{token}", method = RequestMethod.GET)
    public Map get(@PathVariable String token) {
        String url = gumgaValues.getGumgaSecurityUrl() + "/public/token/" + token;
        Map resposta = restTemplate.getForObject(url, Map.class);
        return resposta;
    }

    @RequestMapping(method = RequestMethod.PUT)
    public Map changePassword(@RequestBody UserAndPassword login) {
        String url = gumgaValues.getGumgaSecurityUrl() + "/public/token";
        restTemplate.put(url, login);
        return GumgaSecurityCode.OK.response();
    }

    @Transactional
    @RequestMapping("/organizations/{token}")
    public List organizations(@PathVariable String token) {
        String url = gumgaValues.getGumgaSecurityUrl() + "/public/token/organizations/" + token;
        List resposta = restTemplate.getForObject(url, List.class);
        return resposta;
    }

    @Transactional
    @RequestMapping("/operations/{token}/{software}")
    public Set operations(@PathVariable String token, @PathVariable String software) {
        String url = gumgaValues.getGumgaSecurityUrl() + "/public/token/operations/" + token + "/" + software;
        Set resposta = restTemplate.getForObject(url, Set.class);
        return resposta;
    }

}
