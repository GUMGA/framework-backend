package gumga.framework.domain;
import gumga.framework.domain.GumgaModel;

import javax.persistence.Entity;


@Entity
public class Car extends GumgaModel {
	
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
