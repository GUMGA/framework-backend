package gumga.framework.domain.tag;

import gumga.framework.domain.GumgaModel;
import gumga.framework.domain.GumgaMultitenancy;
import java.util.*;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * Representa a definição de um TAG
 *
 * @author munif
 */
@Entity
@SequenceGenerator(name = GumgaModel.SEQ_NAME, sequenceName = "SEQ_GUMGA_TAG_DEF")
@Table(name = "gumga_tag_def")
@GumgaMultitenancy
public class GumgaTagDefinition extends GumgaModel<Long> {

    @Version
    private Integer version;

    @NotNull
    private String name;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<GumgaTagValueDefinition> attributes = new ArrayList<>();

    public GumgaTagDefinition() {
    }

    public GumgaTagDefinition(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<GumgaTagValueDefinition> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<GumgaTagValueDefinition> attributes) {
        this.attributes = attributes;
    }

    public void addAttribute(String name) {
        if (attributes == null) {
            attributes = new ArrayList<>();
        }
        attributes.add(new GumgaTagValueDefinition(name));
    }

    public void addAttributes(String... names) {
        for (String name : names) {
            addAttribute(name);
        }
    }
}
