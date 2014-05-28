package gumga.framework.security;


import javax.servlet.ServletContext;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.multipart.support.MultipartFilter;
import org.springframework.web.servlet.support.AbstractDispatcherServletInitializer;

public class SecurityApplicationInitializer extends AbstractSecurityWebApplicationInitializer {
	
	@Override
	protected String getDispatcherWebApplicationContextSuffix() {
		return AbstractDispatcherServletInitializer.DEFAULT_SERVLET_NAME;
	}

 	@Override
	protected void afterSpringSecurityFilterChain(ServletContext servletContext) {
		insertFilters(servletContext, new HiddenHttpMethodFilter(), new MultipartFilter());
	}

	@Override
	protected boolean enableHttpSessionEventPublisher() {
		return true;
	}

}
