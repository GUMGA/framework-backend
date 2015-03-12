/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gumga.framework.presentation.api;

import gumga.framework.application.GumgaUntypedRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author munif
 */
@RestController
@RequestMapping("/public/multisearch/")
public class MultiSearchApi {

    @Autowired
    private GumgaUntypedRepository gur;

    public void setGur(GumgaUntypedRepository gur) {
        this.gur = gur;
    }

    @Transactional
    @RequestMapping("search/{clazz}/{text}")
    public List<GumgaGenericResult> search(@PathVariable String clazz, @PathVariable String text) {
        System.out.println("--------search ------>");
        List<GumgaGenericResult> aRetornar = new ArrayList<>();
        try {
            List<Object> resultado = gur.fullTextSearch(text, Class.forName(clazz), "descricao", "valor", "ativo");
            for (Object obj : resultado) {
                aRetornar.add(new GumgaGenericResult(obj));
            }
            return aRetornar;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MultiSearchApi.class.getName()).log(Level.SEVERE, null, ex);
        }
        return aRetornar;

    }

}
