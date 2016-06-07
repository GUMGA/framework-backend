/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gumga.framework.security;

/**
 *
 * @author munif
 */
@FunctionalInterface
public interface ApiOperationTranslator {

    String getOperation(String url, String method);

}
