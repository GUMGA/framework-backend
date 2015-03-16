/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gumga.framework.security;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author munif
 */
public enum GumgaSecurityCode {

    OK,
    UNDEFINED,
    NO_USER,
    BAD_PASSWORD,
    NO_INSTANCE,
    NO_TOKEN,
    TOKEN_EXPIRED,
    NO_ORGANIZATION,
    OPERATION_NOT_ALLOWED;

    public Map response() {
        Map toReturn = new HashMap();
        toReturn.put("response", toString());
        return toReturn;
    }

}
