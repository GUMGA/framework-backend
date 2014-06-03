package gumga.framework.domain;

import javax.persistence.Entity;

@Entity
public class Company extends GumgaModel {
	
	private String name;
	
	public Company() {
	}
	
	public Company(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

}
