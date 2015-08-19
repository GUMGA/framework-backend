package gumga.framework.application.spring.config;

import gumga.framework.domain.GumgaQueryParserProvider;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class OracleDataSourceProvider implements DataSourceProvider {

    @Override
    public DataSource createDataSource(String url, String user, String password) {
        return createDataSource(url, user, password, 5, 20);
    }
    
    @Override
    public DataSource createDataSource(String url, String user, String password, int minConnections, int maxConnections) {
        HikariConfig config = new HikariConfig();
        GumgaQueryParserProvider.defaultMap = GumgaQueryParserProvider.getOracleLikeMap();
        config.setDataSourceClassName("oracle.jdbc.pool.OracleDataSource");
        config.addDataSourceProperty("url", url);
        config.addDataSourceProperty("user", user);
        config.addDataSourceProperty("password", password);
        config.setMinimumIdle(minConnections);
        config.setMaximumPoolSize(maxConnections);
        config.setIdleTimeout(30000L);
        config.setInitializationFailFast(true);
        return new HikariDataSource(config);
    }

    @Override
    public String getDialect() {
        return "org.hibernate.dialect.Oracle10gDialect";
    }

}
