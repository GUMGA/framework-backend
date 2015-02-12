/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gumga.framework.domain;

import gumga.framework.core.GumgaThreadScope;
import org.hibernate.envers.RevisionListener;

/**
 *
 * @author munif
 */
public class GumgaRevisionListener implements RevisionListener {

    @Override
    public void newRevision(Object o) {
        GumgaRevisionEntity gre = (GumgaRevisionEntity) o;
        gre.setUserLogin(GumgaThreadScope.login.get());
        gre.setIp(GumgaThreadScope.ip.get());
    }

}
