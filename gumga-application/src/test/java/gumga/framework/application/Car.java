package gumga.framework.application;

import gumga.framework.domain.GumgaModel;
import gumga.framework.domain.GumgaMultitenancy;
import gumga.framework.domain.customfields.GumgaCustomizableModel;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;


@Entity
@SequenceGenerator(name = GumgaModel.SEQ_NAME, sequenceName = "SEQ_CAR")
@GumgaMultitenancy
public class Car extends GumgaCustomizableModel<Long> {
	
	public Car() {
		
	}
	
	public Car(String color) {
		this.color = color;
	}
	
	private String color;

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
	

}
