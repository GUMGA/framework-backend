/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gumga.framework.security;

import java.lang.annotation.Annotation;
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

    private final RestTemplate restTemplate;

    private final String GUMGASECURITY_AUTORIZE_ENDPOINT = "http://localhost:8084/gumgasecurity-presentation/public/token/authorize";

    @Autowired
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
        restTemplate = new RestTemplate();
    }

    @Override
    public boolean preHandle(HttpServletRequest requset, HttpServletResponse response, Object o) throws Exception {

        String token = "no token";
        try {
            token = requset.getHeader("gumgaToken");
            if (token == null) {//TIRAR DAQUI!!!!
                token = requset.getParameter("gumgaToken");
            }
            String endPoint = requset.getRequestURL().toString();
            String method = requset.getMethod();
            String operationKey = aot.getOperation(endPoint, method);

            if (operationKey.equals("NOOP")) {
                HandlerMethod hm = (HandlerMethod) o;
                OperationKey methodAnnotation = hm.getMethodAnnotation(OperationKey.class);
                if (methodAnnotation == null) {
                    methodAnnotation = new OperationKey() {

                        @Override
                        public String value() {
                            String apiName = hm.getBean().getClass().getSimpleName();
                            apiName = apiName.substring(0, apiName.indexOf("$$"));
                            return apiName + "_" + hm.getMethod().getName();
                        }

                        @Override
                        public Class<? extends Annotation> annotationType() {
                            return OperationKey.class;
                        }
                    };
                }
                operationKey = methodAnnotation.value();
            }

            String url = GUMGASECURITY_AUTORIZE_ENDPOINT + "/" + token + "/" + operationKey + "/" + requset.getRemoteAddr().replace('.', '_');

            AuthorizatonResponse ar = restTemplate.getForObject(url, AuthorizatonResponse.class);
            GumgaThreadScope.login.set(ar.getLogin());
            GumgaThreadScope.ip.set(requset.getRemoteAddr());
            GumgaThreadScope.organization.set(ar.getOrganization());
            GumgaThreadScope.organizationCode.set(ar.getOrganizationCode());
            GumgaThreadScope.operationKey.set(operationKey);

            //System.out.println("##### GumgaRequestFilter preHandle-----> " + GumgaThreadScope.login.get() + " " + GumgaThreadScope.ip.get() + " " + GumgaThreadScope.organization.get() + " " + GumgaThreadScope.organizationCode.get() + " " + methodAnnotation.value());
            if (ar.isAllowed() || endPoint.contains("public")) {
                return true;
            }
        } catch (Exception ex) {
            System.out.println("!@#$%*&$#$%*( " + ex.toString());

        } finally {

        }
        response.setStatus(401);
        response.getOutputStream().write("Not allowed".getBytes());
        return false;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        GumgaThreadScope.ip.remove();
        GumgaThreadScope.login.remove();
        GumgaThreadScope.organization.remove();
        GumgaThreadScope.organizationCode.remove();
        GumgaThreadScope.operationKey.remove();
    }

}
