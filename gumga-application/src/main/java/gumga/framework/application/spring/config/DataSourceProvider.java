package gumga.framework.application.spring.config;

import javax.sql.DataSource;

/**
 * Interface describing the behavior of an data source provider.
 * There should be one implementation per database type.
 * 
 * @author gyowanny
 * @since jul/2015
 * 
 * @param <EM> The type of the entity manager provider
 */
public interface DataSourceProvider {

	/**
	 * Creates the data source
	 * @return a data source instance
	 */
	public DataSource createDataSource(String url, String user, String password);
	
	/**
	 * Returns the name of the dialect
	 * @return
	 */
	public String getDialect();
}
