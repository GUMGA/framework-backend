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
public class GumgaThreadScope {

    public final static ThreadLocal<String> ip = new ThreadLocal<>();
    public final static ThreadLocal<String> login = new ThreadLocal<>();
    public final static ThreadLocal<String> organization = new ThreadLocal<>();
    public final static ThreadLocal<String> organizationCode = new ThreadLocal<>();
    public final static ThreadLocal<String> operationKey = new ThreadLocal<>();
    public final static ThreadLocal<Boolean> ignoreCheckOwnership = new ThreadLocal<>();

}
