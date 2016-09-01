/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gumga.framework.security;

import com.wordnik.swagger.annotations.ApiOperation;
import gumga.framework.core.FacebookRegister;
import gumga.framework.core.GumgaValues;
import gumga.framework.core.UserAndPassword;

import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

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

    @ApiOperation(value = "create", notes = "Cria token através do usuário e senha informados.")
    @RequestMapping(value = "/create/{user}/{password}", method = RequestMethod.GET)
    public ResponseEntity create(@PathVariable String user, @PathVariable String password) {
        String url = gumgaValues.getGumgaSecurityUrl() + "/token/create/" + user + "/" + password;
        Map resposta = restTemplate.getForObject(url, Map.class);
        GumgaSecurityCode response = GumgaSecurityCode.OK; //TODO ESTÁ PARA MANTER COMPATÍVEL COM A VERSÃO ANTERIOR DO SEGURANÇA, 
        if (resposta.containsKey("response")) {
            response = GumgaSecurityCode.valueOf("" + resposta.get("response"));
        }
        return new ResponseEntity(resposta, response.httpStatus);
    }

    @ApiOperation(value = "delete", notes = "Faz logout do usuário fazendo o token informado expirar.")
    @RequestMapping(value = "/{token}", method = RequestMethod.DELETE)
    public Map delete(@PathVariable String token) {
        String url = gumgaValues.getGumgaSecurityUrl() + "/token/" + token;
        restTemplate.delete(url);
        return GumgaSecurityCode.OK.response();
    }

    @ApiOperation(value = "login", notes = "Faz o login recebendo o objeto UserAndPassword.")
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

    @ApiOperation(value = "facebook", notes = "Faz o login com facebook recebendo email e token.")
    @RequestMapping(value="/facebook", method = RequestMethod.GET)
    public Map loginWithFacebook(@RequestParam("email") String email,@RequestParam("token") String facebookToken) {
        String url = gumgaValues.getGumgaSecurityUrl() + "/token/facebook?email="+email+"&token="+facebookToken;
        System.out.print(url);
        Map resposta = restTemplate.getForObject(url, Map.class);
        return resposta;
    }

    @ApiOperation(value = "facebook", notes = "Faz o login com github recebendo email e token.")
    @RequestMapping(value="/github", method = RequestMethod.GET)
    public Map loginWithGitHub(@RequestParam("email") String email,@RequestParam("token") String gitToken) {
        String url = gumgaValues.getGumgaSecurityUrl() + "/token/github?email="+email+"&token="+gitToken;
        System.out.print(url);
        Map resposta = restTemplate.getForObject(url, Map.class);
        return resposta;
    }

    @ApiOperation(value = "register-facebook", notes = "Cria usuário e organização com facebook")
    @RequestMapping(value="/register-facebook", method = RequestMethod.POST)
    public Map loginWithFacebook(@RequestBody FacebookRegister facebookRegister) {
        String url = gumgaValues.getGumgaSecurityUrl() + "/token/register-facebook";
        Map resposta = restTemplate.postForObject(url, facebookRegister, Map.class);
        return resposta;
    }

    @RequestMapping(value = "/{token}", method = RequestMethod.GET)
    public Map get(@PathVariable String token) {
        String url = gumgaValues.getGumgaSecurityUrl() + "/public/token/" + token;
        Map resposta = restTemplate.getForObject(url, Map.class);
        return resposta;
    }

    @ApiOperation(value = "changePassword", notes = "Altera a senha do usuário informados pelo objeto UserAndPassword.")
    @RequestMapping(method = RequestMethod.PUT)
    public Map changePassword(@RequestBody UserAndPassword login) {
        String url = gumgaValues.getGumgaSecurityUrl() + "/token";
        restTemplate.put(url, login);
        return GumgaSecurityCode.OK.response();
    }

    @Transactional
    @ApiOperation(value = "organizations", notes = "Lista as organizações associadas ao token informado.")
    @RequestMapping(value = "/organizations/{token}", method = RequestMethod.GET)
    public List organizations(@PathVariable String token) {
        String url = gumgaValues.getGumgaSecurityUrl() + "/token/organizations/" + token;
        List resposta = restTemplate.getForObject(url, List.class);
        return resposta;
    }

    @Transactional
    @ApiOperation(value = "organizations", notes = "Lista as operações associadas ao software e token informados.")
    @RequestMapping(value = "/operations/{software}/{token:.+}", method = RequestMethod.GET)
    public Set operations(@PathVariable String software, @PathVariable String token) {
        String url = gumgaValues.getGumgaSecurityUrl() + "/token/operations/" + software + "/" + token + "/";
        Set resposta = restTemplate.getForObject(url, Set.class);
        return resposta;
    }

    @ApiOperation(value = "lostPassword", notes = "Permite recuperar a senha, enviando um e-mail para o login informado.")
    @RequestMapping(method = RequestMethod.GET, value = "/lostpassword/{login:.+}")
    public Map lostPassword(@PathVariable String login) {
        String url = gumgaValues.getGumgaSecurityUrl() + "/token/lostpassword/" + login + "/";
        Map resposta = restTemplate.getForObject(url, Map.class);
        return resposta;
    }

    @ApiOperation(value = "lostSoftwarePassword", notes = "Permite recuperar a senha, enviando um e-mail para o login informado.")
    @RequestMapping(method = RequestMethod.GET, value = "/lostsoftwarepassword/{software}/{login:.+}")
    public Map lostSoftwarePassword(@PathVariable String software,@PathVariable String login) {
        String url = gumgaValues.getGumgaSecurityUrl() + "/token/lostsoftwarepassword/" + software + "/"+login + "/";
        Map resposta = restTemplate.getForObject(url, Map.class);
        return resposta;
    }

    @ApiOperation(value = "changeByTicket", notes = "Verifica se o ticket já foi utilizado e altera a senha do usuário.")
    @RequestMapping(method = RequestMethod.GET, value = "/lostpassword/{code}/{password}")
    public Map changeByTicket(@PathVariable String code, @PathVariable String password) {
        String url = gumgaValues.getGumgaSecurityUrl() + "/token/lostpassword/" + code + "/" + password;
        Map resposta = restTemplate.getForObject(url, Map.class);
        return resposta;
    }

    @ApiOperation(value = "findByTicket", notes = "Busca um ticket pelo código.")
    @RequestMapping(method = RequestMethod.GET, value = "/searchticket/{code}")
    public Map findByTicket(@PathVariable String code) {
        String url = gumgaValues.getGumgaSecurityUrl() + "/token/searchticket/" + code;
        Map resposta = restTemplate.getForObject(url, Map.class);
        return resposta;
    }

    @ApiOperation(value = "/organizations/users", notes = "Buscar todos os usuarios por organização.")
    @RequestMapping(method = RequestMethod.GET, value = "/organizations/users/{token}")
    public List findAllUserByOrganization(@PathVariable String token) {

        final String url = gumgaValues.getGumgaSecurityUrl() + "/token/organization/users?gumgaToken=" + token;

        List result = this.restTemplate.getForObject(url, List.class);

        return result;
    }

    @ApiOperation(value = "/roles", notes = "Buscar todos os perfis.")
    @RequestMapping(method = RequestMethod.GET, value = "/roles")
    public List getAllRoles() {
        final String url = gumgaValues.getGumgaSecurityUrl() + "/token/roles";
        List result = this.restTemplate.getForObject(url, List.class);
        return result;
    }

}
