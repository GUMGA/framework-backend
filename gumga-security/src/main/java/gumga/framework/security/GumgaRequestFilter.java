/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gumga.framework.security;

import gumga.framework.application.GumgaLogService;
import gumga.framework.core.GumgaThreadScope;
import gumga.framework.domain.GumgaLog;
import java.util.Collection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 *
 * @author munif
 */
public class GumgaRequestFilter extends HandlerInterceptorAdapter {

    private final String softwareId;

    private final RestTemplate restTemplate;

    @Autowired
    private GumgaLogService gls;
    private Environment environment;
    private Boolean isLogActive;
    private String securityURL;

    private ThreadLocal<Long> tempo = new ThreadLocal<>();

    @Autowired(required = false)
    private ApiOperationTranslator aot = new ApiOperationTranslator() {

        @Override
        public String getOperation(String url, String method) {
            return "NOOP";
        }
    };

    @Autowired(required = false)
    public void setEnvironment(Environment environment) {
        this.environment = environment;
        this.isLogActive = this.environment.getProperty("gumga.log.active", Boolean.class, Boolean.TRUE);
        this.securityURL = environment.getProperty("gumga.security.url", "http://localhost:8084/gumgasecurity-presentation");
    }

    public void setAot(ApiOperationTranslator aot) {
        this.aot = aot;
    }

    public GumgaRequestFilter() {
        softwareId = "SomeSoftware";
        restTemplate = new RestTemplate();
    }

    public GumgaRequestFilter(String si) {
        softwareId = si;
        restTemplate = new RestTemplate();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        tempo.set(System.currentTimeMillis());
        String token = "no token";
        String errorMessage = "Error";
        try {
            token = request.getHeader("gumgaToken");
            if (token == null) {
                token = request.getParameter("gumgaToken");
            }
            String endPoint = request.getRequestURL().toString();
            String method = request.getMethod();
            if (endPoint.contains("public")) {
                saveLog(new AuthorizatonResponse("allow", "public", "public", "public", "public", "public"), request, "", endPoint, method);
                return true;
            }
            String operationKey = "NOOP";
            HandlerMethod hm = (HandlerMethod) o;
            GumgaOperationKey gumgaOperationKeyMethodAnnotation = hm.getMethodAnnotation(GumgaOperationKey.class);
            if (gumgaOperationKeyMethodAnnotation != null) {
                operationKey = gumgaOperationKeyMethodAnnotation.value();
            } else {
                operationKey = aot.getOperation(endPoint, method);
            }
            if (operationKey.equals("NOOP")) {
                String apiName = hm.getBean().getClass().getSimpleName();
                if (apiName.contains("$$")) {
                    apiName = apiName.substring(0, apiName.indexOf("$$"));
                }
                operationKey = apiName + "_" + hm.getMethod().getName();
            }
            String url = securityURL + "/public/token/authorize/" + softwareId + "/" + token + "/" + operationKey + "/" + request.getRemoteAddr().replace('.', '_');

            AuthorizatonResponse ar = restTemplate.getForObject(url, AuthorizatonResponse.class);
            GumgaThreadScope.login.set(ar.getLogin());
            GumgaThreadScope.ip.set(request.getRemoteAddr());
            GumgaThreadScope.organization.set(ar.getOrganization());
            GumgaThreadScope.organizationCode.set(ar.getOrganizationCode());
            GumgaThreadScope.operationKey.set(operationKey);

            saveLog(ar, request, operationKey, endPoint, method);
            if (ar.isAllowed()) {
                return true;
            } else {
                errorMessage = ar.getReason();
            }
        } catch (Exception ex) {
            System.out.println("!@#$%*&$#$%*( " + ex.toString());
            ex.printStackTrace();
        } finally {

        }
        response.setStatus(401);
        response.getOutputStream().write(errorMessage.getBytes());
        return false;
    }

    public void saveLog(AuthorizatonResponse ar, HttpServletRequest requset, String operationKey, String endPoint, String method) {
        if (isLogActive) {
            GumgaLog gl = new GumgaLog(ar.getLogin(), requset.getRemoteAddr(), ar.getOrganizationCode(),
                    ar.getOrganization(), softwareId, operationKey, endPoint, method);
            gls.save(gl);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//        System.out.println((System.currentTimeMillis() - tempo.get()) + "ms " + request.getRemoteHost() + " " + request.getRequestURI() + " " + request.getMethod());
        tempo.remove();
        GumgaThreadScope.ip.remove();
        GumgaThreadScope.login.remove();
        GumgaThreadScope.organization.remove();
        GumgaThreadScope.organizationCode.remove();
        GumgaThreadScope.operationKey.remove();
    }

}
