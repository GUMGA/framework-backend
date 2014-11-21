/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gumga.framework.domain.domains.usertypes;

import gumga.framework.domain.domains.GumgaAddress;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import org.hibernate.usertype.CompositeUserType;

/**
 *
 * @author munif
 */
public class GumgaAddressUserType implements CompositeUserType {

    @Override
    public String[] getPropertyNames() {
        return new String[]{"cep", "tipoLogradouro", "logradouro", "numero", "complemento", "bairro", "localidade", "uf", "pais"};
    }

    @Override
    public Type[] getPropertyTypes() {
        return new Type[]{StringType.INSTANCE, StringType.INSTANCE, StringType.INSTANCE, StringType.INSTANCE, StringType.INSTANCE,
            StringType.INSTANCE, StringType.INSTANCE, StringType.INSTANCE, StringType.INSTANCE};
    }

    @Override
    public Object getPropertyValue(final Object component, final int property) throws HibernateException {
        switch (property) {
            case 0:
                return ((GumgaAddress) component).getCep();
            case 1:
                return ((GumgaAddress) component).getTipoLogradouro();
            case 2:
                return ((GumgaAddress) component).getLogradouro();
            case 3:
                return ((GumgaAddress) component).getNumero();
            case 4:
                return ((GumgaAddress) component).getComplemento();
            case 5:
                return ((GumgaAddress) component).getBairro();
            case 6:
                return ((GumgaAddress) component).getLocalidade();
            case 7:
                return ((GumgaAddress) component).getUf();
            case 8:
                return ((GumgaAddress) component).getPais();

        }
        return null;
    }

    @Override
    public void setPropertyValue(final Object component, final int property, final Object setValue) throws HibernateException {
        switch (property) {
            case 0:
                ((GumgaAddress) component).setCep((String) setValue);
            case 1:
                ((GumgaAddress) component).setTipoLogradouro((String) setValue);
            case 2:
                ((GumgaAddress) component).setLogradouro((String) setValue);
            case 3:
                ((GumgaAddress) component).setNumero((String) setValue);
            case 4:
                ((GumgaAddress) component).setComplemento((String) setValue);
            case 5:
                ((GumgaAddress) component).setBairro((String) setValue);
            case 6:
                ((GumgaAddress) component).setLocalidade((String) setValue);
            case 7:
                ((GumgaAddress) component).setUf((String) setValue);
            case 8:
                ((GumgaAddress) component).setPais((String) setValue);

        }
    }

    @Override
    public Class returnedClass() {
        return GumgaAddress.class;
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
        GumgaAddress object = null;
        final String cep = resultSet.getString(names[0]);
        //Deferred check after first read
        if (!resultSet.wasNull()) {
            object = new GumgaAddress(cep, resultSet.getString(names[1]), resultSet.getString(names[2]), resultSet.getString(names[3]), resultSet.getString(names[4]), resultSet.getString(names[5]), resultSet.getString(names[6]), resultSet.getString(names[7]), resultSet.getString(names[8]));
        }
        return object;
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
            preparedStatement.setNull(property + 0, java.sql.Types.VARCHAR);
            preparedStatement.setNull(property + 1, java.sql.Types.VARCHAR);
            preparedStatement.setNull(property + 2, java.sql.Types.VARCHAR);
            preparedStatement.setNull(property + 3, java.sql.Types.VARCHAR);
            preparedStatement.setNull(property + 4, java.sql.Types.VARCHAR);
            preparedStatement.setNull(property + 5, java.sql.Types.VARCHAR);
            preparedStatement.setNull(property + 6, java.sql.Types.VARCHAR);
            preparedStatement.setNull(property + 7, java.sql.Types.VARCHAR);
            preparedStatement.setNull(property + 8, java.sql.Types.VARCHAR);
        } else {
            final GumgaAddress object = (GumgaAddress) value;
            preparedStatement.setString(property + 0, object.getCep());
            preparedStatement.setString(property + 1, object.getTipoLogradouro());
            preparedStatement.setString(property + 2, object.getLogradouro());
            preparedStatement.setString(property + 3, object.getNumero());
            preparedStatement.setString(property + 4, object.getComplemento());
            preparedStatement.setString(property + 5, object.getBairro());
            preparedStatement.setString(property + 6, object.getLocalidade());
            preparedStatement.setString(property + 7, object.getUf());
            preparedStatement.setString(property + 8, object.getPais());
        }
    }

    /**
     * Used to create Snapshots of the object
     */
    @Override
    public Object deepCopy(final Object value) throws HibernateException {
//        return value; if object was immutable we could return the object as its is
        final GumgaAddress recebido = (GumgaAddress) value;
        final GumgaAddress aRetornar = new GumgaAddress(recebido);
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
