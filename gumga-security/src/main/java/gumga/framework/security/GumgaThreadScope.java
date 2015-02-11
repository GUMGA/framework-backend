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
public class GumgaThreadScope {

    public static ThreadLocal<String> ip = new ThreadLocal<>();
    public static ThreadLocal<String> login = new ThreadLocal<>();
    public static ThreadLocal<String> organization = new ThreadLocal<>();
    public static ThreadLocal<String> organizationCode = new ThreadLocal<>();
    public static ThreadLocal<String> operationKey = new ThreadLocal<>();

}
