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

    default String getGumgaSecurityUrl(){
        return "http://www.gumga.com.br/security/publicoperations";
    }

    default long getDefaultTokenDuration() { 
        return 30 * 60 * 1000;
    }

    default boolean isLogActive(){
        return true;
    }

    default String getUploadTempDir() {
        return System.getProperty("user.home").concat("/gumgafiles/tempupload");

    }

    default String getLogDir() {
        return System.getProperty("user.home").concat("/gumgafiles/logs");
    }

}
