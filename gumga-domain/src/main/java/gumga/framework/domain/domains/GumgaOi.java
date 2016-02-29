package gumga.framework.domain.domains;

import com.google.common.base.Objects;
import gumga.framework.domain.GumgaMultitenancy;

/**
 * Representa o código organizacional, fundamental para o Multitenancy.
 *
 * @author munif
 */
public class GumgaOi extends GumgaDomain implements Comparable<GumgaOi> {

    private String value;

    protected GumgaOi() {
    }

    public GumgaOi(String value) {
        this.value = value;
    }

    public GumgaOi(GumgaOi other) {
        if (other != null) {
            this.value = other.value;
        }

    }

    public String getValue() {
        return value;
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
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
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
