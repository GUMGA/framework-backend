/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gumga.framework.presentation.api;

/**
 *
 * @author munif
 */
public class AuthorizatonResponse {

    private String response;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public boolean isAllowed() {
        return "allow".equals(response);
    }

}
