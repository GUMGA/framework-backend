package gumga.framework.application.spring.config;

import gumga.framework.domain.GumgaQueryParserProvider;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * Data source for the MySql database
 * 
 * @author gyowanny
 *
 */
public class MySqlDataSourceProvider implements DataSourceProvider {

	@Override
	public DataSource createDataSource(String url, String user, String password) {
		HikariConfig config = new HikariConfig();
        GumgaQueryParserProvider.defaultMap = GumgaQueryParserProvider.getMySqlLikeMap();
        config.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
        config.addDataSourceProperty("url", url);
        config.addDataSourceProperty("user", user);
        config.addDataSourceProperty("password", password);	
        config.setMaximumPoolSize(120);
        config.setIdleTimeout(30000L);
        config.setInitializationFailFast(true);

        return new HikariDataSource(config);
	}

	@Override
	public String getDialect() {
		//return "org.hibernate.dialect.MySQL5Dialect";
		return "org.hibernate.dialect.MySQLDialect";
	}


}
