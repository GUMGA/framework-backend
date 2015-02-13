/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gumga.framework.domain;

import gumga.framework.core.GumgaIdable;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;

/**
 *
 * @author munif
 */
@Entity
public class GumgaLog implements GumgaIdable<Long> {

    @Id
    @GeneratedValue
    private Long id;
    private String login;
    private String ip;
    private String organizationCode;
    private String organization;
    private String software;
    private String operarationKey;
    private String endPoint;
    private String method;
    private String oi;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date quando;

    public GumgaLog() {
        this.quando = new Date();
    }

    public GumgaLog(String login, String ip, String organizationCode, String organization, String software, String operarationKey, String endPoint, String method) {
        this.login = login;
        this.ip = ip;
        this.organizationCode = organizationCode;
        this.organization = organization;
        this.software = software;
        this.operarationKey = operarationKey;
        this.endPoint = endPoint;
        this.method = method;
        this.quando = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getOrganizationCode() {
        return organizationCode;
    }

    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getSoftware() {
        return software;
    }

    public void setSoftware(String software) {
        this.software = software;
    }

    public String getOperarationKey() {
        return operarationKey;
    }

    public void setOperarationKey(String operarationKey) {
        this.operarationKey = operarationKey;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Date getQuando() {
        return quando;
    }

    public void setQuando(Date quando) {
        this.quando = quando;
    }

    public String getOi() {
        return oi;
    }

    public void setOi(String oi) {
        this.oi = oi;
    }

    @Override
    public String toString() {
        return "GumgaLog{" + "id=" + id + ", login=" + login + ", ip=" + ip + ", organizationCode=" + organizationCode + ", organization=" + organization + ", software=" + software + ", operarationKey=" + operarationKey + ", endPoint=" + endPoint + ", method=" + method + '}';
    }

}
