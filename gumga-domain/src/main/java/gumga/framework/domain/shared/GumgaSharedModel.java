/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gumga.framework.domain.shared;

import gumga.framework.domain.GumgaModel;
import gumga.framework.domain.domains.GumgaOi;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 *
 * @author munif, mateus, felipe
 */
@MappedSuperclass
public class GumgaSharedModel<ID extends Serializable> extends GumgaModel<ID> {

    @Column(name = "gumga_orgs", length = 4096)
    private String gumgaOrganizations;

    @Column(name = "gumga_users", length = 4096)
    private String gumgaUsers;

    public GumgaSharedModel() {
        init();
    }

    public GumgaSharedModel(GumgaOi oi) {
        super(oi);
        init();
    }

    public String getGumgaOrganizations() {
        return gumgaOrganizations;
    }

    public String getGumgaUsers() {
        return gumgaUsers;
    }

    private final void init() {
        gumgaOrganizations = ",";
        gumgaUsers = ",";
    }

    public void addOrganization(String oi) {
        gumgaOrganizations = StringList.add(gumgaOrganizations, oi);
    }

    public void addUser(String login) {
        gumgaUsers = StringList.add(gumgaUsers, login);
    }

    public void removeOrganization(String oi) {
        gumgaOrganizations = StringList.remove(gumgaOrganizations, oi);
    }

    public void removeUser(String login) {
        gumgaUsers = StringList.remove(gumgaUsers, login);
    }

    public void removeAllOrganization() {
        gumgaOrganizations = StringList.removeAll();
    }

    public void removeAllUser() {
        gumgaUsers = StringList.removeAll();
    }

}

class StringList {

    public static String add(String base, String value) {
        if (!contains(base, value)) {
            return base + value + ",";
        }
        return base;
    }

    public static String remove(String base, String value) {
        return base.replaceAll("," + value + ",", ",");
    }

    public static String removeAll() {
        return ",";
    }

    private static boolean contains(String base, String value) {
        return base.contains("," + value + ",");
    }

}

//  ,abacaxi,laranja,fanta-laranja,coca,guarana,
// ,coca,guarana,

