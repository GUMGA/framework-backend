package gumga.framework.security;


import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.web.servlet.support.AbstractDispatcherServletInitializer;

public class SecurityApplicationInitializer extends AbstractSecurityWebApplicationInitializer {
	
	@Override
	protected String getDispatcherWebApplicationContextSuffix() {
		return AbstractDispatcherServletInitializer.DEFAULT_SERVLET_NAME;
	}

	@Override
	protected boolean enableHttpSessionEventPublisher() {
		return true;
	}

}
