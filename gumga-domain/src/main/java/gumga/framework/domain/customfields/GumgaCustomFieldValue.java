/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gumga.framework.domain.customfields;

import gumga.framework.core.JavaScriptEngine;
import gumga.framework.domain.GumgaModel;
import gumga.framework.domain.GumgaMultitenancy;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 * Armazena o valor de um atributo genérico
 *
 * @author munif
 */
@Entity
@SequenceGenerator(name = GumgaModel.SEQ_NAME, sequenceName = "SEQ_GUMGA_FLD_VLE")
//             123456789012345678
@Table(name = "gumga_fld_vle")
@GumgaMultitenancy
public class GumgaCustomFieldValue extends GumgaModel<Long> {

    //TEXT, NUMBER, DATE, LOGIC, SELECTION;
    @ManyToOne
    private GumgaCustomField field;

    private Long gumgaModelId;

    private String textValue;
    private BigDecimal numberValue;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateValue;
    private Boolean logicValue;

    public GumgaCustomFieldValue() {
    }

    public GumgaCustomFieldValue(GumgaCustomField cf) {
        this.field = cf;
        if (CustomFieldType.DATE.equals(cf.getType())) {
            setValue(JavaScriptEngine.evalForDate(cf.getDefaultValueScript(), null));
        } else {
            setValue(JavaScriptEngine.eval(cf.getDefaultValueScript(), null));
        }
    }

    public GumgaCustomFieldValue(GumgaCustomField field, Long gumgaModelId, Object value) {
        this.field = field;
        this.gumgaModelId = gumgaModelId;
        setValue(value);
    }

    public GumgaCustomField getField() {
        return field;
    }

    public void setField(GumgaCustomField field) {
        this.field = field;
    }

    public Long getGumgaModelId() {
        return gumgaModelId;
    }

    public void setGumgaModelId(Long gumgaModelId) {
        this.gumgaModelId = gumgaModelId;
    }

    public String getTextValue() {
        return textValue;
    }

    public void setTextValue(String textValue) {
        this.textValue = textValue;
    }

    public BigDecimal getNumberValue() {
        return numberValue;
    }

    public void setNumberValue(BigDecimal numberValue) {
        this.numberValue = numberValue;
    }

    public Date getDateValue() {
        return dateValue;
    }

    public void setDateValue(Date dateValue) {
        this.dateValue = dateValue;
    }

    public Boolean getLogicValue() {
        return logicValue;
    }

    public void setLogicValue(Boolean logicValue) {
        this.logicValue = logicValue;
    }

    public void setValue(Object value) {
        switch (field.getType()) {
            case DATE:
                dateValue = (Date) value;
                break;
            case LOGIC:
                logicValue = (Boolean) value;
                break;
            case NUMBER:
                if (value instanceof BigDecimal) {
                    numberValue = (BigDecimal) value;
                } else {
                    numberValue = new BigDecimal(value.toString());
                }
                break;
            case SELECTION:
            case TEXT:
                textValue = (String) value;
                break;
        }
    }

    public Object getValue() {
        switch (field.getType()) {
            case DATE:
                return dateValue;
            case LOGIC:
                return logicValue;
            case NUMBER:
                return numberValue;
            case SELECTION:
            case TEXT:
                return textValue;
        }
        return null;
    }

    @Override
    public String toString() {
        Object value = getValue();
        return value == null ? "null" : value.toString();
    }

}
