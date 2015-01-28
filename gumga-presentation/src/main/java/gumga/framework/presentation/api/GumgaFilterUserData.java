/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gumga.framework.presentation.api;

import java.io.IOException;
import java.lang.annotation.Annotation;
import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author munif
 */
@WebFilter(urlPatterns = "*")
public class GumgaFilterUserData implements Filter {
    
    public static ThreadLocal<String> ip = new ThreadLocal<>();
    public static ThreadLocal<String> user = new ThreadLocal<>();
    
    @Override
    public void init(FilterConfig fc) throws ServletException {
    }
    
    @Override
    public void doFilter(ServletRequest sr, ServletResponse sr1, FilterChain fc) throws IOException, ServletException {
        try {
            HttpServletRequest hsq = (HttpServletRequest) sr;
            ip.set(hsq.getRemoteAddr());
            user.set(hsq.getUserPrincipal() != null ? hsq.getUserPrincipal().getName() : "not logged");
            fc.doFilter(sr, sr1);
        } catch (ClassCastException ex) {
            //Ignore
        } finally {
            
        }
    }
    
    @Override
    public void destroy() {
        
    }
    
}
