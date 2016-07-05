/*
    This file is part of Opes.

    Opes is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Opes is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with Opes.  If not, see <http://www.gnu.org/licenses/>.
 */
package gumga.framework.domain.domains.usertypes;

import gumga.framework.domain.domains.GumgaYoutubeURL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import org.hibernate.engine.spi.SessionImplementor;

/**
 * UserType que permite serializar o tipo de URL do Youtube
 */
public class GumgaYoutubeURLUserType extends ImmutableUserType {

    private static final long serialVersionUID = 1L;

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws SQLException {
        String value = rs.getString(names[0]);
        if (rs.wasNull()) {
            return null;
        } else {
            return GumgaYoutubeURL.fromString(value);
        }
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session)
            throws SQLException {
        if (value == null) {
            st.setNull(index, Types.VARCHAR);
        } else {
            GumgaYoutubeURL url = (GumgaYoutubeURL) value;
            st.setString(index, url.toString());
        }
    }

    @Override
    public Class<?> returnedClass() {
        return GumgaYoutubeURL.class;
    }

    @Override
    public int[] sqlTypes() {
        return new int[]{Types.VARCHAR};
    }

}
