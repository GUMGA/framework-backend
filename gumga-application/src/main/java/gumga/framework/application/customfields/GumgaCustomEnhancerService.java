package gumga.framework.application.customfields;

import gumga.framework.core.GumgaIdable;
import gumga.framework.domain.GumgaModel;
import gumga.framework.domain.customfields.GumgaCustomField;
import gumga.framework.domain.customfields.GumgaCustomFieldValue;
import gumga.framework.domain.customfields.GumgaCustomizableModel;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author munif
 */
@Service
public class GumgaCustomEnhancerService {

    @Autowired
    private GumgaCustomFieldService customFieldService;

    @Autowired
    private GumgaCustomFieldValueService customFieldValueService;

    public void setDefaultValues(Object object) {
        if (!(object instanceof GumgaCustomizableModel)) {
            return;
        }
        GumgaCustomizableModel gumgaCustomizable = (GumgaCustomizableModel) object;
        List<GumgaCustomField> customFields = customFieldService.findByClass(gumgaCustomizable.getClass());
        for (GumgaCustomField cf : customFields) {
            GumgaCustomFieldValue newValue = newValue(cf);
            gumgaCustomizable.getGumgaCustomFields().put(cf.getName(), newValue);
        }
    }

    /**
     * Criar uma nova instancia de um attributo generico
     * @param cf
     * @return
     */
    public GumgaCustomFieldValue newValue(GumgaCustomField cf) {
        return new GumgaCustomFieldValue(cf);
    }

    /**
     * Carregar os attributos genericos do objeto
     * @param gumgaModel
     */
    public void loadCustomFields(Object gumgaModel) {
        if (!(gumgaModel instanceof GumgaCustomizableModel)) {
            return;
        }
        GumgaCustomizableModel gumgaCustomizable = (GumgaCustomizableModel) gumgaModel;
        List<GumgaCustomField> customFields = customFieldService.findByClass(gumgaModel.getClass());
        for (GumgaCustomField cf : customFields) {
            Object value = customFieldValueService.getValue(cf, (GumgaModel) gumgaModel);
            if (value == null) {
                value = newValue(cf);
            }
            gumgaCustomizable.getGumgaCustomFields().put(cf.getName(), value);
        }
    }

    /**
     * Salvar atributos genericos
     * @param gumgaModel
     */
    public void saveCustomFields(GumgaIdable gumgaModel) {
        if (!(gumgaModel instanceof GumgaCustomizableModel)) {
            return;
        }
        GumgaCustomizableModel gumgaCustomizable = (GumgaCustomizableModel) gumgaModel;
        Set<String> keySet = gumgaCustomizable.getGumgaCustomFields().keySet();
        for (String s : keySet) {
            GumgaCustomFieldValue value = (GumgaCustomFieldValue) gumgaCustomizable.getGumgaCustomFields().get(s);
            if (value != null) {
                value.setGumgaModelId((Long) gumgaModel.getId());
                customFieldValueService.save(value);
            }
        }

    }

    /**
     * Remover atributos genericos
     * @param gumgaModel
     */
    public void deleteCustomFields(GumgaIdable gumgaModel) {
        if (!(gumgaModel instanceof GumgaCustomizableModel)) {
            return;
        }
        GumgaCustomizableModel gumgaCustomizable = (GumgaCustomizableModel) gumgaModel;
        Set<String> keySet = gumgaCustomizable.getGumgaCustomFields().keySet();
        for (String s : keySet) {
            GumgaCustomFieldValue value = (GumgaCustomFieldValue) gumgaCustomizable.getGumgaCustomFields().get(s);
            if (value != null) {
                customFieldValueService.delete(value);
            }
        }

    }

}
