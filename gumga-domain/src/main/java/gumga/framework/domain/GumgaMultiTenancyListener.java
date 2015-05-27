/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gumga.framework.domain;

import gumga.framework.domain.domains.GumgaOi;
import gumga.framework.core.GumgaThreadScope;
import javax.persistence.PrePersist;

/**
 *
 * @author munif
 */
public class GumgaMultiTenancyListener {

    @PrePersist
    public void prePersist(GumgaModel gumgaModel) {
        Class classe = gumgaModel.getClass();
        if (classe.isAnnotationPresent(GumgaMultitenancy.class)) {
            String oc = GumgaThreadScope.organizationCode.get();
            gumgaModel.oi = new GumgaOi(oc);
        }
    }
}
