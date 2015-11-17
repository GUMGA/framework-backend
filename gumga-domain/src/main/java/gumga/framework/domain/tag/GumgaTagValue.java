/*
 * Gerado automaticamente por GUMGAGenerator em 16/11/2015 16:42:10
 */
package gumga.framework.domain.tag;

import gumga.framework.domain.GumgaModel;
import gumga.framework.domain.GumgaMultitenancy;
import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@SequenceGenerator(name = GumgaModel.SEQ_NAME, sequenceName = "SEQ_GUMGA_TAG_VAL")
//             123456789012345678
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

}
