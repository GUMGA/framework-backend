package gumga.framework.presentation;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@ComponentScan({"gumga.framework"})
@EnableWebMvc
public class SpringConfig {
	
	@Autowired
	BeanFactory factory;
	
	@Bean
	public static PropertyPlaceholderConfigurer dataConfigPropertyConfigurer() {
		PropertyPlaceholderConfigurer configurer = new PropertyPlaceholderConfigurer();
		configurer.setSearchSystemEnvironment(true);
		return configurer;
	}

	
}
