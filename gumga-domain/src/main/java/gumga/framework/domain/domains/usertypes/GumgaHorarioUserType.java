/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gumga.framework.domain.domains.usertypes;

import gumga.framework.domain.domains.GumgaHorario;
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
public class GumgaHorarioUserType implements CompositeUserType {

    @Override
    public String[] getPropertyNames() {
        return new String[]{"hora", "minuto", "segundo"};
    }

    @Override
    public Type[] getPropertyTypes() {
        return new Type[]{IntegerType.INSTANCE, IntegerType.INSTANCE, IntegerType.INSTANCE};
    }

    @Override
    public Object getPropertyValue(final Object component, final int property) throws HibernateException {
        switch (property) {
            case 0:
                return ((GumgaHorario) component).getHora();
            case 1:
                return ((GumgaHorario) component).getMinuto();
            case 2:
                return ((GumgaHorario) component).getSegundo();
        }
        return null;
    }

    @Override
    public void setPropertyValue(final Object component, final int property, final Object setValue) throws HibernateException {
        switch (property) {
            case 0:
                ((GumgaHorario) component).setHora((Integer) setValue);
            case 1:
                ((GumgaHorario) component).setMinuto((Integer) setValue);
            case 2:
                ((GumgaHorario) component).setSegundo((Integer) setValue);
        }
    }

    @Override
    public Class returnedClass() {
        return GumgaHorario.class;
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
        GumgaHorario gumgaHorario = null;
        final Integer hora = resultSet.getInt(names[0]);
        //Deferred check after first read
        if (!resultSet.wasNull()) {
            gumgaHorario = new GumgaHorario(hora, resultSet.getInt(names[1]), resultSet.getInt(names[2]));
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
            final GumgaHorario horario = (GumgaHorario) value;
            preparedStatement.setInt(property, horario.getHora());
            preparedStatement.setInt(property + 1, horario.getMinuto());
            preparedStatement.setInt(property + 2, horario.getSegundo());
        }
    }

    /**
     * Used to create Snapshots of the object
     */
    @Override
    public Object deepCopy(final Object value) throws HibernateException {
//        return value; if object was immutable we could return the object as its is
        final GumgaHorario recievedParam = (GumgaHorario) value;
        final GumgaHorario auditData = new GumgaHorario(recievedParam);        
        return auditData;
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
