/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gumga.framework.core;

/**
 * Claasse que contem o escopo GUMGA, acessível de qualquer camada que é
 * preenchida pelo interceptor. Seu uso é recomendado na Integração e em
 * consultas não triviais que necessitam do O.I. (Organization Identification),
 * de informações sobre a requisição (Token, IP, Operação) e permite ignorar a
 * checagem de propriedade para perminir a execução de métodos de negócio que
 * permeiam o Tenancy.
 *
 * @author munif
 */
public class GumgaThreadScope {

    /**
     * Ip da requisição
     */
    public final static ThreadLocal<String> ip = new ThreadLocal<>();
    /**
     * login do usuário da requisição
     */
    public final static ThreadLocal<String> login = new ThreadLocal<>();
    /**
     * nome organização do usuário da requisição
     */
    public final static ThreadLocal<String> organization = new ThreadLocal<>();
    /**
     * código da organização do usuário da requisição
     */
    public final static ThreadLocal<String> organizationCode = new ThreadLocal<>();
    /**
     * chave da operação da requisição
     */
    public final static ThreadLocal<String> operationKey = new ThreadLocal<>();
    /**
     * irá ignorar a checagem de propriedade dos registros do Tenacy
     */
    public final static ThreadLocal<Boolean> ignoreCheckOwnership = new ThreadLocal<>();
    /**
     * token da requisição
     */
    public final static ThreadLocal<String> gumgaToken = new ThreadLocal<>();

    /**
     * @return o parâmetro a ser utilizado na comparação do Tenancy
     */
    public final static String getOiWildCard() {
        if (organizationCode.get() == null) {
            return "%";
        }
        return organizationCode.get().concat("%");
    }

}
