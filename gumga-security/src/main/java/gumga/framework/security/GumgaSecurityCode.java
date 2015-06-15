/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gumga.framework.security;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;

/**
 *
 * @author munif
 */
public enum GumgaSecurityCode {

    OK(HttpStatus.OK),
    ALLOW(HttpStatus.OK),
    UNDEFINED(HttpStatus.I_AM_A_TEAPOT),
    NO_USER(HttpStatus.UNAUTHORIZED),
    BAD_PASSWORD(HttpStatus.UNAUTHORIZED),
    NO_INSTANCE(HttpStatus.FORBIDDEN),
    NO_TOKEN(HttpStatus.FORBIDDEN),
    TOKEN_EXPIRED(HttpStatus.FORBIDDEN),
    NO_ORGANIZATION(HttpStatus.FORBIDDEN),
    OPERATION_NOT_ALLOWED(HttpStatus.UNAUTHORIZED),
    INSTANCE_EXPIRED(HttpStatus.FORBIDDEN),
    IP_NOT_ALLOWED(HttpStatus.FORBIDDEN),
    TIME_NOT_ALLOWED(HttpStatus.FORBIDDEN),
    SECURITY_INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR);

    public HttpStatus httpStatus;

    GumgaSecurityCode(HttpStatus s) {
        httpStatus = s;
    }

    public Map response() {
        Map toReturn = new HashMap();
        toReturn.put("response", this);
        return toReturn;
    }

}
