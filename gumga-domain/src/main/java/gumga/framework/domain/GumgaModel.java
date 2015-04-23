package gumga.framework.domain;

import java.io.Serializable;

import gumga.framework.core.GumgaIdable;
import gumga.framework.domain.domains.*;
import gumga.framework.domain.domains.usertypes.*;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.EntityListeners;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.MappedSuperclass;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

@MappedSuperclass
@TypeDefs({
    @TypeDef(name = "gumgaaddress", defaultForType = GumgaAddress.class, typeClass = GumgaAddressUserType.class),
    @TypeDef(name = "gugmaboolean", defaultForType = GumgaBoolean.class, typeClass = GumgaBooleanUserType.class),
    @TypeDef(name = "gumgacep", defaultForType = GumgaCEP.class, typeClass = GumgaCEPUserType.class),
    @TypeDef(name = "gumgacnpj", defaultForType = GumgaCNPJ.class, typeClass = GumgaCNPJUserType.class),
    @TypeDef(name = "gumgacpf", defaultForType = GumgaCPF.class, typeClass = GumgaCPFUserType.class),
    @TypeDef(name = "gumgaemail", defaultForType = GumgaEMail.class, typeClass = GumgaEMailUserType.class),
    @TypeDef(name = "gumgafile", defaultForType = GumgaFile.class, typeClass = GumgaFileUserType.class),
    @TypeDef(name = "gumgageolocation", defaultForType = GumgaGeoLocation.class, typeClass = GumgaGeoLocationUserType.class),
    @TypeDef(name = "gumgaip4", defaultForType = GumgaIP4.class, typeClass = GumgaIP4UserType.class),
    @TypeDef(name = "gumgaip6", defaultForType = GumgaIP6.class, typeClass = GumgaIP6UserType.class),
    @TypeDef(name = "gumgaimage", defaultForType = GumgaImage.class, typeClass = GumgaImageUserType.class),
    @TypeDef(name = "gumgamoney", defaultForType = GumgaMoney.class, typeClass = GumgaMoneyUserType.class),
    @TypeDef(name = "gumgamutilinestring", defaultForType = GumgaMultiLineString.class, typeClass = GumgaMultiLineStringUserType.class),
    @TypeDef(name = "gumgaphonenumber", defaultForType = GumgaPhoneNumber.class, typeClass = GumgaPhoneNumberUserType.class),
    @TypeDef(name = "gumgatime", defaultForType = GumgaTime.class, typeClass = GumgaTimeUserType.class),
    @TypeDef(name = "gumgaoi", defaultForType = GumgaOi.class, typeClass = GumgaOiUserType.class),
    @TypeDef(name = "gumgaurl", defaultForType = GumgaURL.class, typeClass = GumgaURLUserType.class)
})
@EntityListeners(GumgaMultiTenancyListener.class)
public abstract class GumgaModel<ID extends Serializable> implements GumgaIdable<ID>, Serializable {

    public static final String SEQ_NAME = "SEQ";
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)//, generator = SEQ_NAME)
    protected ID id;

    protected GumgaOi oi;

    public GumgaModel() {
        // Construtor vazio para serialização
    }

    public GumgaModel(GumgaOi oi) {
        this.oi = oi;
    }

    public ID getId() {
        return id;
    }

    public GumgaOi getOi() {
        return oi;
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
