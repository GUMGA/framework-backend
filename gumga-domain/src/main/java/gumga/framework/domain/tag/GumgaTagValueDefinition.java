package gumga.framework.domain.tag;

import gumga.framework.domain.GumgaModel;
import gumga.framework.domain.GumgaMultitenancy;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * Representa a definição de um valor para TAGs
 *
 * @author munif
 */
@Entity
@SequenceGenerator(name = GumgaModel.SEQ_NAME, sequenceName = "SEQ_GTAG_VALU_DEFI")
@Table(name = "gtag_vdefi")
@GumgaMultitenancy
public class GumgaTagValueDefinition extends GumgaModel<Long> {

    @Version
    private Integer version;

    @NotNull
    private String name;

    public GumgaTagValueDefinition() {
    }

    public GumgaTagValueDefinition(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
