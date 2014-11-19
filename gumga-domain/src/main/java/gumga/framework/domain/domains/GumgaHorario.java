/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gumga.framework.domain.domains;

import gumga.framework.domain.domains.usertypes.GumgaHorarioUserType;
import java.util.Calendar;
import org.hibernate.annotations.TypeDef;

/**
 *
 * @author munif
 */
public final class GumgaHorario extends GumgaDomain {

    private int hora;
    private int minuto;
    private int segundo;

    public GumgaHorario() {
        Calendar agora = Calendar.getInstance();
        hora = agora.get(Calendar.HOUR_OF_DAY);
        minuto = agora.get(Calendar.MINUTE);
        segundo = agora.get(Calendar.SECOND);
    }

    public GumgaHorario(int hora, int minuto) {
        setHora(hora);
        setMinuto(minuto);
        setSegundo(0);
    }

    public GumgaHorario(int hora, int minuto, int segundo) {
        setHora(hora);
        setMinuto(minuto);
        setSegundo(segundo);
    }

    public GumgaHorario(GumgaHorario origem) {
        if (origem != null) {
            setHora(origem.getHora());
            setMinuto(origem.getMinuto());
            setSegundo(origem.getSegundo());
        }
    }

    public int getHora() {
        return hora;
    }

    public void setHora(int hora) {
        if (hora >= 0 && hora <= 23) {
            this.hora = hora;
        }
    }

    public int getMinuto() {
        return minuto;
    }

    public void setMinuto(int minuto) {
        if (minuto > 0 && minuto <= 59) {
            this.minuto = minuto;
        }
    }

    public int getSegundo() {
        return segundo;
    }

    public void setSegundo(int segundo) {
        if (segundo >= 0 && segundo <= 59) {
            this.segundo = segundo;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + this.hora;
        hash = 11 * hash + this.minuto;
        hash = 11 * hash + this.segundo;
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
        final GumgaHorario other = (GumgaHorario) obj;
        if (this.hora != other.hora) {
            return false;
        }
        if (this.minuto != other.minuto) {
            return false;
        }
        if (this.segundo != other.segundo) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return (hora > 10 ? hora : "0" + hora) + ":" + (minuto > 10 ? minuto : "0" + minuto) + ":" + (segundo > 10 ? segundo : "0" + segundo);
    }

}
