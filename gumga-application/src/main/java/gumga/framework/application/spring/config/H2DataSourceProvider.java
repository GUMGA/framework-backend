package gumga.framework.application.spring.config;

import gumga.framework.domain.GumgaQueryParserProvider;

import javax.sql.DataSource;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * Data source provider for H2 database
 * 
 * @author gyowanny
 *
 */
public class H2DataSourceProvider implements DataSourceProvider {

	@Override
	public DataSource createDataSource(String url, String user, String password) {
		HikariConfig config = new HikariConfig();
        GumgaQueryParserProvider.defaultMap = GumgaQueryParserProvider.getH2LikeMap();
        config.setDataSourceClassName("org.h2.jdbcx.JdbcDataSource");
        config.addDataSourceProperty("url", url);
        config.addDataSourceProperty("user", user);
        config.addDataSourceProperty("password", password);
        config.setMaximumPoolSize(20);
        config.setIdleTimeout(30000L);
        config.setInitializationFailFast(true);
        return new HikariDataSource(config);
	}

	@Override
	public String getDialect() {
		return "org.hibernate.dialect.H2Dialect";
	}

}
