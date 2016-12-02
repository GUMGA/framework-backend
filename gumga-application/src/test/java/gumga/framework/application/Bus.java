package gumga.framework.application;

import gumga.framework.domain.GumgaModel;
import gumga.framework.domain.GumgaMultitenancy;
import gumga.framework.domain.customfields.GumgaCustomizableModel;
import gumga.framework.domain.shared.GumgaSharedModel;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;

@Entity
@SequenceGenerator(name = GumgaModel.SEQ_NAME, sequenceName = "SEQ_BUS")
@GumgaMultitenancy
public class Bus extends GumgaSharedModel<Long> {

    public Bus() {

    }

    public Bus(String line) {
        this.line = line;
    }

    private String line;

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

}
