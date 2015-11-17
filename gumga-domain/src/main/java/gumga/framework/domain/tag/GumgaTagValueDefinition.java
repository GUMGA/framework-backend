/*
 * Gerado automaticamente por GUMGAGenerator em 16/11/2015 16:42:01
 */
package gumga.framework.domain.tag;

import gumga.framework.domain.GumgaModel;
import gumga.framework.domain.GumgaMultitenancy;
import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@SequenceGenerator(name = GumgaModel.SEQ_NAME, sequenceName = "SEQ_GUMGA_TAG_VAL_DEF")
//             123456789012345678
@Table(name = "gumga_tag_vdef")
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
