package com.upm.notes.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.net.URI;

@Configuration
public class DatabaseConfig {

    private static final Logger log = LoggerFactory.getLogger(DatabaseConfig.class);

    @Bean
    @Primary
    public DataSource dataSource() {
        String dbUrl = System.getenv("DB_URL");

        if (dbUrl != null && !dbUrl.isBlank()) {
            log.info("Detected DB_URL environment variable, configuring PostgreSQL DataSource...");
            try {
                String normalized = dbUrl.trim();
                if (normalized.startsWith("postgresql://")) {
                    normalized = "postgres://" + normalized.substring("postgresql://".length());
                }

                if (normalized.startsWith("postgres://")) {
                    URI uri = new URI(normalized);
                    String host = uri.getHost();
                    int port = (uri.getPort() > 0) ? uri.getPort() : 5432;
                    String database = uri.getPath();

                    String jdbcUrl = "jdbc:postgresql://" + host + ":" + port + database;

                    String username = null;
                    String password = null;
                    if (uri.getUserInfo() != null) {
                        String[] userParts = uri.getUserInfo().split(":", 2);
                        username = userParts[0];
                        if (userParts.length > 1) {
                            password = userParts[1];
                        }
                    }

                    HikariConfig config = new HikariConfig();
                    config.setJdbcUrl(jdbcUrl);
                    config.setDriverClassName("org.postgresql.Driver");
                    if (username != null) config.setUsername(username);
                    if (password != null) config.setPassword(password);

                    config.setMaximumPoolSize(10);
                    config.setMinimumIdle(2);
                    config.setConnectionTimeout(15000);

                    log.info("Successfully configured PostgreSQL connection to {}:{}", host, port);
                    return new HikariDataSource(config);
                } else if (normalized.startsWith("jdbc:")) {
                    HikariConfig config = new HikariConfig();
                    config.setJdbcUrl(normalized);
                    return new HikariDataSource(config);
                }
            } catch (Exception e) {
                log.error("Failed to parse DB_URL: {}", e.getMessage(), e);
                throw new IllegalStateException("Invalid DB_URL format: " + dbUrl, e);
            }
        }

        log.info("No DB_URL environment variable found. Initializing local H2 in-memory database.");
        HikariConfig h2Config = new HikariConfig();
        h2Config.setJdbcUrl("jdbc:h2:mem:notesdb;DB_CLOSE_DELAY=-1;MODE=PostgreSQL");
        h2Config.setDriverClassName("org.h2.Driver");
        h2Config.setUsername("sa");
        h2Config.setPassword("");
        return new HikariDataSource(h2Config);
    }
}
