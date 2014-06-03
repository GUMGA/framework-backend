package gumga.framework.domain;
import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.H2;

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan({"gumga.framework"})
@EnableTransactionManagement
public class SpringConfig {
	
	@Autowired
	BeanFactory factory;
	
	@Bean
	public static PropertyPlaceholderConfigurer dataConfigPropertyConfigurer() {
		PropertyPlaceholderConfigurer configurer = new PropertyPlaceholderConfigurer();
		configurer.setSearchSystemEnvironment(true);
		return configurer;
	}
	
	@Bean
	public static DataSource dataSource() {
		return new EmbeddedDatabaseBuilder().setType(H2).build();
	}

	@Bean @Autowired
	public static LocalSessionFactoryBean sessionFactory(DataSource dataSource) {
		Properties hibernateProperties = new Properties();
		hibernateProperties.put("hibernate.hbm2ddl.auto", "create-drop");
		hibernateProperties.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
		hibernateProperties.put("hibernate.ejb.naming_strategy", "org.hibernate.cfg.ImprovedNamingStrategy");
		hibernateProperties.put("hibernate.show_sql", "true");
		hibernateProperties.put("hibernate.format_sql", "true");
		hibernateProperties.put("hibernate.connection.charSet", "UTF-8");
		hibernateProperties.put("hibernate.connection.characterEncoding", "UTF-8");
		hibernateProperties.put("hibernate.connection.useUnicode", "true");
		hibernateProperties.put("hibernate.jdbc.batch_size", "50");
		
		final LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		sessionFactory.setDataSource(dataSource);
		sessionFactory.setPackagesToScan("gumga.framework.domain");
		sessionFactory.setHibernateProperties(hibernateProperties);

		return sessionFactory;
	}

	@Bean @Autowired
	public static HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
		HibernateTransactionManager txManager = new HibernateTransactionManager(sessionFactory);
		txManager.setNestedTransactionAllowed(true);
		return txManager;
	}
	
}
