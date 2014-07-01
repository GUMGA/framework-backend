package gumga.framework.core;

import java.io.Serializable;

public interface GumgaIdable<T extends Serializable> {
	
	T getId();

}
