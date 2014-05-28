package gumga.framework.security;


import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@Order(1)
public class RestSecurityConfig extends WebSecurityConfigurerAdapter {

	protected void configure(HttpSecurity http) throws Exception {
		http.antMatcher("/api/**").exceptionHandling().authenticationEntryPoint(new RestAuthenticationEntryPoint())
			.and().authorizeRequests().anyRequest().hasRole("USER").and().httpBasic();
		
		http.csrf().disable();
	}

}
