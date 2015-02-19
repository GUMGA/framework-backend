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
public class AuthorizatonResponse {

    private String response;
    private String organization;
    private String organizationCode;
    private String login;
    private String reason;
    private String key;

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public boolean isAllowed() {
        return "allow".equals(response);
    }

    public String getOrganizationCode() {
        return organizationCode;
    }

    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "AuthorizatonResponse{" + "response=" + response + ", organization=" + organization + ", organizationCode=" + organizationCode + ", login=" + login + ", reason=" + reason + ", key=" + key + '}';
    }

}
