package gumga.framework.domain.domains;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

import java.io.Serializable;

/**
 * Organization Instance
 */
public class GumgaOi implements Serializable, Comparable<GumgaOi> {

    private String value;

    protected GumgaOi() {
        // Construtor vazio para serialização
    }

    public GumgaOi(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GumgaOi that = (GumgaOi) o;

        return Objects.equal(this.value, that.value);
    }

    @Override
    public int compareTo(GumgaOi o) {
        if (o == null || o.value == null) {
            return (this.value == null) ? 0 : 1;
        }

        if (this.value == null) {
            return -1;
        }

        return this.value.compareTo(o.value);


    }
}
