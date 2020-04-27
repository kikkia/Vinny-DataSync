package com.data.config;

import com.data.config.properties.DBProperties;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class DBConfig {

    @Bean
    public HikariDataSource hikari(DBProperties properties) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:mysql://" + properties.getUri() + "/"  + properties.getSchema() + "?useSSL=false");
        hikariConfig.setUsername(properties.getUser());
        hikariConfig.setPassword(properties.getPassword());
        hikariConfig.setIdleTimeout(600*1000);
        hikariConfig.setMaxLifetime(900*1000);
        hikariConfig.setMaximumPoolSize(properties.getPoolSize());
        hikariConfig.setMinimumIdle(2);
        hikariConfig.setLeakDetectionThreshold(5 * 1000);
        hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "150");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        hikariConfig.addDataSourceProperty("useServerPrepStmts", "true");
        return new HikariDataSource(hikariConfig);
    }
}
