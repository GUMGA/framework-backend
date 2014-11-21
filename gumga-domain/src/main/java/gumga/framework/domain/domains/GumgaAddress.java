/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gumga.framework.domain.domains;

import java.util.Objects;

/**
 *
 * @author munif
 */
public class GumgaAddress extends GumgaDomain {

    private String cep;
    private String tipoLogradouro;
    private String logradouro;
    private String numero;
    private String complemento;
    private String bairro;
    private String localidade;
    private String uf;
    private String pais;

    public GumgaAddress() {

    }

    public GumgaAddress(String cep, String tipoLogradouro, String logradouro, String numero, String complemento, String bairro, String localidade, String uf, String pais) {
        this.cep = cep;
        this.tipoLogradouro = tipoLogradouro;
        this.logradouro = logradouro;
        this.numero = numero;
        this.complemento = complemento;
        this.bairro = bairro;
        this.localidade = localidade;
        this.uf = uf;
        this.pais = pais;
    }

    public GumgaAddress(GumgaAddress other) {
        if (other != null) {
            this.cep = other.cep;
            this.tipoLogradouro = other.tipoLogradouro;
            this.logradouro = other.logradouro;
            this.numero = other.numero;
            this.complemento = other.complemento;
            this.bairro = other.bairro;
            this.localidade = other.localidade;
            this.uf = other.uf;
            this.pais = other.pais;
        }
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getTipoLogradouro() {
        return tipoLogradouro;
    }

    public void setTipoLogradouro(String tipoLogradouro) {
        this.tipoLogradouro = tipoLogradouro;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getLocalidade() {
        return localidade;
    }

    public void setLocalidade(String localidade) {
        this.localidade = localidade;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + Objects.hashCode(this.cep);
        hash = 83 * hash + Objects.hashCode(this.tipoLogradouro);
        hash = 83 * hash + Objects.hashCode(this.logradouro);
        hash = 83 * hash + Objects.hashCode(this.numero);
        hash = 83 * hash + Objects.hashCode(this.complemento);
        hash = 83 * hash + Objects.hashCode(this.bairro);
        hash = 83 * hash + Objects.hashCode(this.localidade);
        hash = 83 * hash + Objects.hashCode(this.uf);
        hash = 83 * hash + Objects.hashCode(this.pais);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GumgaAddress other = (GumgaAddress) obj;
        if (!Objects.equals(this.cep, other.cep)) {
            return false;
        }
        if (!Objects.equals(this.tipoLogradouro, other.tipoLogradouro)) {
            return false;
        }
        if (!Objects.equals(this.logradouro, other.logradouro)) {
            return false;
        }
        if (!Objects.equals(this.numero, other.numero)) {
            return false;
        }
        if (!Objects.equals(this.complemento, other.complemento)) {
            return false;
        }
        if (!Objects.equals(this.bairro, other.bairro)) {
            return false;
        }
        if (!Objects.equals(this.localidade, other.localidade)) {
            return false;
        }
        if (!Objects.equals(this.uf, other.uf)) {
            return false;
        }
        if (!Objects.equals(this.pais, other.pais)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "GumgaAddress{" + "cep=" + cep + ", tipoLogradouro=" + tipoLogradouro + ", logradouro=" + logradouro + ", numero=" + numero + ", complemento=" + complemento + ", bairro=" + bairro + ", localidade=" + localidade + ", uf=" + uf + ", pais=" + pais + '}';
    }

}
