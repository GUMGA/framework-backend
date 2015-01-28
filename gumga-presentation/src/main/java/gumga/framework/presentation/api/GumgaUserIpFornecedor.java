/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gumga.framework.presentation.api;

import gumga.framework.domain.GumgaRevisionEntity;
import gumga.framework.domain.GumgaRevisionListener;
import gumga.framework.domain.GumgaUserDataQueryable;
import org.springframework.stereotype.Component;

/**
 *
 * @author munif
 */
@Component
public class GumgaUserIpFornecedor implements GumgaUserDataQueryable {

    public GumgaUserIpFornecedor() {
        GumgaRevisionListener.setGudq(this);
    }

    @Override
    public String getIp() {
        return GumgaFilterUserData.ip.get();
    }

    @Override
    public String getLogin() {
        return GumgaFilterUserData.user.get();
    }

}
