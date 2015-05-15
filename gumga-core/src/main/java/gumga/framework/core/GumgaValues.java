/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gumga.framework.core;

/**
 *
 * @author munif
 */
public interface GumgaValues {

    String getGumgaSecurityUrl();

    default long getDefaultTokenDuration() { //TODO em JAVA 8 os métodos das interfaces podem ter corpo
        return 30 * 60 * 1000;
    }

    boolean isLogActive();

}
