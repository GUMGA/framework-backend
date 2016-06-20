/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gumga.framework.presentation.api;

import com.wordnik.swagger.annotations.ApiOperation;
import gumga.framework.application.GumgaUntypedRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author munif
 */
@RestController
@RequestMapping("/api/multisearch/")
public class MultiSearchApi {

    @Autowired
    private GumgaUntypedRepository gur;

    public void setGur(GumgaUntypedRepository gur) {
        this.gur = gur;
    }

    @Transactional
    @ApiOperation(value = "search", notes = "Faz uma pesquisa múltipla com o texto informado.")
    @RequestMapping(value="search/{text}",method = RequestMethod.GET)
    public List<GumgaGenericResult> search(@PathVariable String text) {
        List<GumgaGenericResult> aRetornar = new ArrayList<>();
        List<Object> resultado = gur.fullTextSearch(text);
        for (Object obj : resultado) {
            aRetornar.add(new GumgaGenericResult(obj));
        }
        return aRetornar;
    }

}
