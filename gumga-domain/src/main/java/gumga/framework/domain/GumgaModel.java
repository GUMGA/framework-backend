package gumga.framework.domain;

import java.io.Serializable;

import gumga.framework.core.GumgaIdable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;

import org.apache.commons.lang3.builder.ToStringBuilder;

@MappedSuperclass
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public class GumgaModel<ID extends Serializable> implements GumgaIdable<ID> {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected ID id;
	
	public ID getId() {
		return id;
	}
	
	public void setId(ID id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
}
