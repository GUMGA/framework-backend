package gumga.framework.presentation;

import java.util.HashMap;

public class GumgaSystemProperties extends HashMap<String, String> {

	private static final long serialVersionUID = 1L;

	@Override
    public String get(Object name) {
        return System.getProperty(name != null ? name.toString() : null);
    }

}