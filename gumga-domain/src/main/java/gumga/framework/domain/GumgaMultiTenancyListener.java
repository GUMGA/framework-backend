/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gumga.framework.domain;

import gumga.framework.core.GumgaThreadScope;
import gumga.framework.domain.domains.GumgaOi;
import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

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
            GumgaOi gumgaOi = new GumgaOi(oc);
            gumgaModel.oi = gumgaOi;
        }
    }
}
