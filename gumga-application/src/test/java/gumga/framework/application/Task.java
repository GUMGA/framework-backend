/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gumga.framework.application;

import gumga.framework.domain.GumgaModel;
import gumga.framework.domain.GumgaMultitenancy;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;

/**
 *
 * @author wlademir
 */
@Entity
@SequenceGenerator(name = GumgaModel.SEQ_NAME, sequenceName = "SEQ_CAR")
@GumgaMultitenancy
public class Task extends GumgaModel<Long> implements Serializable{
    
    String name;

    public Task() {
    }

    public Task(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    
    
    
    
    
}
