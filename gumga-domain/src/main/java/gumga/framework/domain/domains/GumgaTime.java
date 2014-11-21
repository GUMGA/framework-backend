package gumga.framework.domain.domains;

import java.util.Calendar;

/**
 *
 * @author munif
 */
public final class GumgaTime extends GumgaDomain {

    private int hour;
    private int minute;
    private int second;

    public GumgaTime() {
        Calendar now = Calendar.getInstance();
        hour = now.get(Calendar.HOUR_OF_DAY);
        minute = now.get(Calendar.MINUTE);
        second = now.get(Calendar.SECOND);
    }

    public GumgaTime(int hour, int minute) {
        setHour(hour);
        setMinute(minute);
        GumgaTime.this.setSecond(0);
    }

    public GumgaTime(int hour, int minute, int second) {
        setHour(hour);
        setMinute(minute);
        GumgaTime.this.setSecond(second);
    }

    public GumgaTime(GumgaTime other) {
        if (other != null) {
            this.hour = other.hour;
            this.minute = other.minute;
            this.second = other.second;
        }
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hora) {
        if (hora >= 0 && hora <= 23) {
            this.hour = hora;
        }
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minuto) {
        if (minuto > 0 && minuto <= 59) {
            this.minute = minuto;
        }
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int segundo) {
        if (segundo >= 0 && segundo <= 59) {
            this.second = segundo;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + this.hour;
        hash = 11 * hash + this.minute;
        hash = 11 * hash + this.second;
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
        final GumgaTime other = (GumgaTime) obj;
        if (this.hour != other.hour) {
            return false;
        }
        if (this.minute != other.minute) {
            return false;
        }
        if (this.second != other.second) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return (hour > 10 ? hour : "0" + hour) + ":" + (minute > 10 ? minute : "0" + minute) + ":" + (second > 10 ? second : "0" + second);
    }

}
