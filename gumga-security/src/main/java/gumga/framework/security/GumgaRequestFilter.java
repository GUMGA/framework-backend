/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gumga.framework.security;

import gumga.framework.application.GumgaLogService;
import gumga.framework.core.GumgaThreadScope;
import gumga.framework.core.GumgaValues;
import gumga.framework.domain.GumgaLog;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.method.HandlerMethod;
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

    @Autowired
    private GumgaValues gumgaValues;

    private ThreadLocal<Long> tempo = new ThreadLocal<>();

    @Autowired(required = false)
    private ApiOperationTranslator aot = new ApiOperationTranslator() {

        @Override
        public String getOperation(String url, String method) {
            return "NOOP";
        }
    };

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
        String token = "not initialized";
        String errorMessage = "Error";
        String errorResponse = GumgaSecurityCode.SECURITY_INTERNAL_ERROR.toString();
        try {
            token = request.getHeader("gumgaToken");
            if (token == null) {
                token = request.getParameter("gumgaToken");
            }
            if (token == null) {
                token = "no token";
            }
            String endPoint = request.getRequestURL().toString();
            String method = request.getMethod();
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
            if (endPoint.contains("public") || endPoint.contains("api-docs")) {
                saveLog(new AuthorizatonResponse("allow", "public", "public", "public", "public", "public"), request, operationKey, endPoint, method, true);
                return true;
            }

            String url = gumgaValues.getGumgaSecurityUrl() + "/token/authorize/" + softwareId + "/" + token + "/" + request.getRemoteAddr() + "/" + operationKey + "/";

            AuthorizatonResponse ar = restTemplate.getForObject(url, AuthorizatonResponse.class);
            GumgaThreadScope.login.set(ar.getLogin());
            GumgaThreadScope.ip.set(request.getRemoteAddr());
            GumgaThreadScope.organization.set(ar.getOrganization());
            GumgaThreadScope.organizationCode.set(ar.getOrganizationCode());
            GumgaThreadScope.operationKey.set(operationKey);
            saveLog(ar, request, operationKey, endPoint, method, ar.isAllowed());
            if (ar.isAllowed()) {
                return true;
            } else {
                errorMessage = ar.toString();
                errorResponse = ar.getResponse();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        GumgaSecurityCode gsc = GumgaSecurityCode.valueOf(errorResponse);
        response.setStatus(gsc.httpStatus.value());
        response.getOutputStream().write(("Error:" + errorMessage).getBytes());
        return false;
    }

    public void saveLog(AuthorizatonResponse ar, HttpServletRequest requset, String operationKey, String endPoint, String method, boolean a) {
        if (gumgaValues.isLogActive()) {
            GumgaLog gl = new GumgaLog(ar.getLogin(), requset.getRemoteAddr(), ar.getOrganizationCode(),
                    ar.getOrganization(), softwareId, operationKey, endPoint, method, a);
            gls.save(gl);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        tempo.remove();
        GumgaThreadScope.ip.remove();
        GumgaThreadScope.login.remove();
        GumgaThreadScope.organization.remove();
        GumgaThreadScope.organizationCode.remove();
        GumgaThreadScope.operationKey.remove();
    }

}
