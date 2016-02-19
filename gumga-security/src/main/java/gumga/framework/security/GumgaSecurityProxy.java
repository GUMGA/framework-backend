/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gumga.framework.security;

import com.wordnik.swagger.annotations.ApiOperation;
import gumga.framework.core.GumgaValues;
import gumga.framework.core.UserAndPassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    @ApiOperation(value = "create",notes = "Cria token através do usuário e senha informados.")
    @RequestMapping(value="/create/{user}/{password}",method = RequestMethod.GET)
    public ResponseEntity create(@PathVariable String user, @PathVariable String password) {
        String url = gumgaValues.getGumgaSecurityUrl() + "/token/create/" + user + "/" + password;
        Map resposta = restTemplate.getForObject(url, Map.class);
        GumgaSecurityCode response = GumgaSecurityCode.OK; //TODO ESTÁ PARA MANTER COMPATÍVEL COM A VERSÃO ANTERIOR DO SEGURANÇA, 
        if (resposta.containsKey("response")) {
            response = GumgaSecurityCode.valueOf("" + resposta.get("response"));
        }
        return new ResponseEntity(resposta, response.httpStatus);
    }

    @ApiOperation(value = "delete",notes = "Faz logout do usuário fazendo o token informado expirar.")
    @RequestMapping(value = "/{token}", method = RequestMethod.DELETE)
    public Map delete(@PathVariable String token) {
        String url = gumgaValues.getGumgaSecurityUrl() + "/token/" + token;
        restTemplate.delete(url);
        return GumgaSecurityCode.OK.response();
    }

    @ApiOperation(value = "login",notes = "Faz o login recebendo o objeto UserAndPassword.")
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity login(@RequestBody UserAndPassword login) {
        String url = gumgaValues.getGumgaSecurityUrl() + "/token";
        Map resposta = restTemplate.postForObject(url, login, Map.class);
        GumgaSecurityCode response = GumgaSecurityCode.OK; //TODO ESTÁ PARA MANTER COMPATÍVEL COM A VERSÃO ANTERIOR DO SEGURANÇA, 
        if (resposta.containsKey("response")) {
            response = GumgaSecurityCode.valueOf("" + resposta.get("response"));
        }
        return new ResponseEntity(resposta, response.httpStatus);
    }

    @RequestMapping(value = "/{token}", method = RequestMethod.GET)
    public Map get(@PathVariable String token) {
        String url = gumgaValues.getGumgaSecurityUrl() + "/public/token/" + token;
        Map resposta = restTemplate.getForObject(url, Map.class);
        return resposta;
    }

    @ApiOperation(value = "changePassword",notes = "Altera a senha do usuário informados pelo objeto UserAndPassword.")
    @RequestMapping(method = RequestMethod.PUT)
    public Map changePassword(@RequestBody UserAndPassword login) {
        String url = gumgaValues.getGumgaSecurityUrl() + "/token";
        restTemplate.put(url, login);
        return GumgaSecurityCode.OK.response();
    }

    @Transactional
    @ApiOperation(value = "organizations", notes = "Lista as organizações associadas ao token informado.")
    @RequestMapping(value="/organizations/{token}",method = RequestMethod.GET)
    public List organizations(@PathVariable String token) {
        String url = gumgaValues.getGumgaSecurityUrl() + "/token/organizations/" + token;
        List resposta = restTemplate.getForObject(url, List.class);
        return resposta;
    }

    @Transactional
    @ApiOperation(value = "organizations", notes = "Lista as operações associadas ao software e token informados.")
    @RequestMapping(value="/operations/{software}/{token}",method = RequestMethod.GET)
    public Set operations(@PathVariable String software, @PathVariable String token) {
        String url = gumgaValues.getGumgaSecurityUrl() + "/token/operations/" + software + "/" + token;
        Set resposta = restTemplate.getForObject(url, Set.class);
        return resposta;
    }

}
