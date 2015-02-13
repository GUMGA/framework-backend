/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gumga.framework.presentation.api;

import gumga.framework.application.GumgaService;
import gumga.framework.domain.GumgaLog;
import gumga.framework.presentation.GumgaAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author munif
 */
@RestController
@RequestMapping("/public/gumgalog")
public class GumgaLogAPI extends GumgaAPI<GumgaLog, Long> {

    @Autowired
    public GumgaLogAPI(GumgaService<GumgaLog, Long> service) {
        super(service);
    }

}
