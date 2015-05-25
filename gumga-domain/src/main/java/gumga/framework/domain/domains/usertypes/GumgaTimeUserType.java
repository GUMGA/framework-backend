/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gumga.framework.domain.domains.usertypes;

import gumga.framework.domain.domains.GumgaTime;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.IntegerType;
import org.hibernate.type.Type;
import org.hibernate.usertype.CompositeUserType;

/**
 * Permite armazenar um horário com hora, minuto e segundo em atributos
 * separados
 *
 * @author munif
 */
public class GumgaTimeUserType implements CompositeUserType {

    @Override
    public String[] getPropertyNames() {
        return new String[]{"hour", "minute", "second"};
    }

    @Override
    public Type[] getPropertyTypes() {
        return new Type[]{IntegerType.INSTANCE, IntegerType.INSTANCE, IntegerType.INSTANCE};
    }

    @Override
    public Object getPropertyValue(final Object component, final int property) throws HibernateException {
        switch (property) {
            case 0:
                return ((GumgaTime) component).getHour();
            case 1:
                return ((GumgaTime) component).getMinute();
            case 2:
                return ((GumgaTime) component).getSecond();
        }
        return null;
    }

    @Override
    public void setPropertyValue(final Object component, final int property, final Object setValue) throws HibernateException {
        switch (property) {
            case 0:
                ((GumgaTime) component).setHour((Integer) setValue);
            case 1:
                ((GumgaTime) component).setMinute((Integer) setValue);
            case 2:
                ((GumgaTime) component).setSecond((Integer) setValue);
        }
    }

    @Override
    public Class returnedClass() {
        return GumgaTime.class;
    }

    @Override
    public boolean equals(final Object o1, final Object o2) throws HibernateException {
        boolean isEqual = false;
        if (o1 == o2) {
            isEqual = false;
        }
        if (null == o1 || null == o2) {
            isEqual = false;
        } else {
            isEqual = o1.equals(o2);
        }
        return isEqual;
    }

    @Override
    public int hashCode(Object o) throws HibernateException {
        return o.hashCode();
    }

    @Override
    public Object nullSafeGet(final ResultSet resultSet,
            final String[] names,
            final SessionImplementor paramSessionImplementor, final Object paramObject)
            throws HibernateException, SQLException {
        //owner here is of type TestUser or the actual owning Object
        GumgaTime gumgaHorario = null;
        final Integer hora = resultSet.getInt(names[0]);
        //Deferred check after first read
        if (!resultSet.wasNull()) {
            gumgaHorario = new GumgaTime(hora, resultSet.getInt(names[1]), resultSet.getInt(names[2]));
        }
        return gumgaHorario;
    }

    /**
     * Before executing the save call this method is called. It will set the
     * values in the prepared statement
     */
    @Override
    public void nullSafeSet(final PreparedStatement preparedStatement,
            final Object value, final int property,
            final SessionImplementor sessionImplementor)
            throws HibernateException, SQLException {
        if (null == value) {
            preparedStatement.setNull(property, java.sql.Types.INTEGER);
            preparedStatement.setNull(property + 1, java.sql.Types.INTEGER);
            preparedStatement.setNull(property + 2, java.sql.Types.INTEGER);
        } else {
            final GumgaTime horario = (GumgaTime) value;
            preparedStatement.setInt(property, horario.getHour());
            preparedStatement.setInt(property + 1, horario.getMinute());
            preparedStatement.setInt(property + 2, horario.getSecond());
        }
    }

    /**
     * Used to create Snapshots of the object
     */
    @Override
    public Object deepCopy(final Object value) throws HibernateException {
        if (value == null) {
            return null;
        }

//        return value; if object was immutable we could return the object as its is
        final GumgaTime recebido = (GumgaTime) value;
        final GumgaTime aRetornar = new GumgaTime(recebido);
        return aRetornar;
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    /**
     * method called when Hibernate puts the data in a second level cache. The
     * data is stored in a serializable form
     */
    @Override
    public Serializable disassemble(final Object value,
            final SessionImplementor paramSessionImplementor)
            throws HibernateException {
        //Thus the data Types must implement serializable
        return (Serializable) value;
    }

    /**
     * Returns the object from the 2 level cache
     */
    @Override
    public Object assemble(final Serializable cached,
            final SessionImplementor sessionImplementor, final Object owner)
            throws HibernateException {
        //would work as the class is Serializable, and stored in cache as it is - see disassemble
        return cached;
    }

    /**
     * Method is called when merging two objects.
     */
    @Override
    public Object replace(final Object original, final Object target,
            final SessionImplementor paramSessionImplementor, final Object owner)
            throws HibernateException {
        //        return original; // if immutable use this
        //For mutable types at bare minimum return a deep copy of first argument
        return this.deepCopy(original);
    }

}
