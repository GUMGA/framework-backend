package gumga.framework.domain.customfields;

import gumga.framework.domain.GumgaModel;
import gumga.framework.domain.GumgaMultitenancy;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author munif
 */
@Entity
@SequenceGenerator(name = GumgaModel.SEQ_NAME, sequenceName = "SEQ_GUMGA_CUSTOM_FIELD")
//             123456789012345678
@Table(name = "gumga_custom_field")
@GumgaMultitenancy
public class GumgaCustomField extends GumgaModel<Long> {

    private String clazz;
    private String name;
    private String description;
    private Boolean active;
    private CustomFieldType type;
    private String validationDescription;
    private String validationScript;
    private String defaultValueScript;
    private String options;
    private Double visualizationOrder;
    private String fieldGroup;

    public GumgaCustomField() {
        this.name = "";
        this.description = "";
        this.active = true;
        this.type = CustomFieldType.TEXT;
        this.validationDescription = "";
        this.validationScript = "return true";
        this.defaultValueScript = "return ''";
        this.options = null;
        this.visualizationOrder = 10.0;
        this.fieldGroup = "$DEFAULT";
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public CustomFieldType getType() {
        return type;
    }

    public void setType(CustomFieldType type) {
        this.type = type;
    }

    public String getValidationDescription() {
        return validationDescription;
    }

    public void setValidationDescription(String validationDescription) {
        this.validationDescription = validationDescription;
    }

    public String getValidationScript() {
        return validationScript;
    }

    public void setValidationScript(String validationScript) {
        this.validationScript = validationScript;
    }

    public String getDefaultValueScript() {
        return defaultValueScript;
    }

    public void setDefaultValueScript(String defaultValueScript) {
        this.defaultValueScript = defaultValueScript;
    }

    public Double getVisualizationOrder() {
        return visualizationOrder;
    }

    public void setVisualizationOrder(Double visualizationOrder) {
        this.visualizationOrder = visualizationOrder;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public String getFieldGroup() {
        return fieldGroup;
    }

    public void setFieldGroup(String fieldGroup) {
        this.fieldGroup = fieldGroup;
    }

    @Override
    public String toString() {
        return name + '(' + clazz + ')';
    }

}
