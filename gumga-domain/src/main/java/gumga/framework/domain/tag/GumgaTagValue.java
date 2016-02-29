package gumga.framework.domain.tag;

import gumga.framework.domain.GumgaModel;
import gumga.framework.domain.GumgaMultitenancy;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * Representa um valor para tags valoráveis
 *
 * @author munif
 */
@Entity
@SequenceGenerator(name = GumgaModel.SEQ_NAME, sequenceName = "SEQ_GUMGA_TAG_VAL")
@Table(name = "gumga_tag_val")
@GumgaMultitenancy
public class GumgaTagValue extends GumgaModel<Long> {

    @Version
    private Integer version;

    @NotNull
    private String value;

    @ManyToOne
    private GumgaTagValueDefinition definition;

    public GumgaTagValue() {
    }

    public GumgaTagValue(GumgaTagValueDefinition definition, String value) {
        this.definition = definition;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public GumgaTagValueDefinition getDefinition() {
        return definition;
    }

    public void setDefinition(GumgaTagValueDefinition definition) {
        this.definition = definition;
    }

    @Override
    public String toString() {
        return "GumgaTagValue{" + "version=" + version + ", value=" + value + ", definition=" + definition + '}';
    }

}
