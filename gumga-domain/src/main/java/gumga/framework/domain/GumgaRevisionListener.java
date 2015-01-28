/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gumga.framework.domain;

import java.util.Date;
import org.hibernate.envers.RevisionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author munif
 */
public class GumgaRevisionListener implements RevisionListener {

    private static GumgaUserDataQueryable gudq = new GumgaUserDataQueryable() {

        @Override
        public String getIp() {
            return "unknow";
        }

        @Override
        public String getLogin() {
            return "unknow";
        }
    };

    public static void setGudq(GumgaUserDataQueryable g) {
        gudq = g;
    }

    @Override
    public void newRevision(Object o) {
        GumgaRevisionEntity gre = (GumgaRevisionEntity) o;
        gre.setUserLogin(gudq.getLogin());
        gre.setIp(gudq.getIp());
    }

}
