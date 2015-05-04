/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gumga.framework.domain.domains.usertypes;

import com.google.common.base.Objects;
import gumga.framework.domain.domains.GumgaDomain;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 *
 * @author munif
 */
public class GumgaOiUserType extends MutableUserType {

    @Override
    public int[] sqlTypes() {
        return new int[]{Types.VARCHAR};
    }

    @Override
    public Class returnedClass() {
        return GumgaOi.class;
    }

    @Override
    public boolean equals(Object o, Object o1) throws HibernateException {
        if (o == null) {
            return false;
        }
        return o.equals(o1);
    }

    @Override
    public int hashCode(Object o) throws HibernateException {
        if (o == null) {
            return 0;
        }
        return o.hashCode();
    }

    @Override
    public Object nullSafeGet(final ResultSet resultSet,
            final String[] names,
            final SessionImplementor paramSessionImplementor, final Object paramObject)
            throws HibernateException, SQLException {
        GumgaOi object = null;
        final String valor = resultSet.getString(names[0]);
        if (!resultSet.wasNull()) {
            object = new GumgaOi(valor);
        }
        return object;
    }

    @Override
    public void nullSafeSet(final PreparedStatement preparedStatement,
            final Object value, final int property,
            final SessionImplementor sessionImplementor)
            throws HibernateException, SQLException {
        if (null == value) {
            preparedStatement.setNull(property, java.sql.Types.VARCHAR);
        } else {
            final GumgaOi object = (GumgaOi) value;
            preparedStatement.setString(property, object.getValue());
        }
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        final GumgaOi recebido = (GumgaOi) value;
        final GumgaOi aRetornar = new GumgaOi(recebido);
        return aRetornar;
    }
}

class GumgaOi extends GumgaDomain implements Comparable<GumgaOi> {

    private String value;

    protected GumgaOi() {
        // Construtor vazio para serialização
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
