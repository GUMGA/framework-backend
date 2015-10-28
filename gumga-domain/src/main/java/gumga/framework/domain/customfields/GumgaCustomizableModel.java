package gumga.framework.domain.customfields;

import gumga.framework.domain.GumgaModel;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

@MappedSuperclass
public class GumgaCustomizableModel<ID extends Serializable>  extends GumgaModel<ID> {

    @Transient
    private final Map<String, GumgaCustomFieldValue> gumgaCustomFields;

    public GumgaCustomizableModel() {
        super();
        gumgaCustomFields = new HashMap<>();
    }

    public Map<String, GumgaCustomFieldValue> getGumgaCustomFields() {
        return gumgaCustomFields;
    }

}
