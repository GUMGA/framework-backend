/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gumga.framework.domain.domains;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 *
 * @author munif
 */
public class GumgaMoney extends GumgaDomain {

    private BigDecimal value;

    public GumgaMoney() {
    }

    public GumgaMoney(BigDecimal value) {
        this.value = value;
    }

    public GumgaMoney(GumgaMoney other) {
        if (other != null) {
            this.value = other.value;
        }
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.value);
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
        final GumgaMoney other = (GumgaMoney) obj;
        if (!Objects.equals(this.value, other.value)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        if (value != null) {
            
            return value.setScale(2, RoundingMode.UP).toString();
        }
        return "NAN";
    }

}
