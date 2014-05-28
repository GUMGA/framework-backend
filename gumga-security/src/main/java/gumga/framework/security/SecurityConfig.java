package gumga.framework.security;


import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.header.writers.CacheControlHeadersWriter;
import org.springframework.security.web.header.writers.HstsHeaderWriter;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	private Logger logger = Logger.getLogger(getClass().getSimpleName());
	
	@Autowired(required = false)
	private UserDetailsService userDetailsService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		if (userDetailsService == null) {
			logger.warning("UserDetailsService not found, Security module has been disabled");
			return;
		}
		
		http.formLogin()
				  .loginPage("/security")
				  .loginProcessingUrl("/security/go")
				  .defaultSuccessUrl("/")
				  .failureUrl("/security?error=true")
				  .usernameParameter("username")
				  .passwordParameter("password")
				  .permitAll(true);

		http.headers()
				  .addHeaderWriter(new XXssProtectionHeaderWriter())
				  .addHeaderWriter(new CacheControlHeadersWriter())
				  .addHeaderWriter(new HstsHeaderWriter());

		http.logout().logoutUrl("/security/logout").invalidateHttpSession(true).logoutSuccessUrl("/security?logout=true");
		
		http.csrf().disable();
		
		http.authorizeRequests().antMatchers("/static/**").permitAll().antMatchers("/security/**").permitAll().anyRequest().authenticated();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		if (userDetailsService == null) {
			logger.warning("UserDetailsService not found, Security module has been disabled");
			return;
		}
		
		auth.userDetailsService(userDetailsService);
	}
		
}

