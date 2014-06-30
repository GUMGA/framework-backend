package gumga.framework.application;

import org.hibernate.Session;

public interface GumgaSessionStrategy {

	public Session getSession();
	
}
