package gumga.framework.domain;

import java.io.Serializable;

import gumga.framework.core.GumgaIdable;
import gumga.framework.domain.domains.GumgaHorario;
import gumga.framework.domain.domains.usertypes.GumgaHorarioUserType;
import java.util.Objects;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

@MappedSuperclass
@TypeDefs({
    @TypeDef(name = "horario", defaultForType = GumgaHorario.class, typeClass = GumgaHorarioUserType.class)
})
public class GumgaModel<ID extends Serializable> implements GumgaIdable<ID> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected ID id;

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.id);
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
        final GumgaModel<?> other = (GumgaModel<?>) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
