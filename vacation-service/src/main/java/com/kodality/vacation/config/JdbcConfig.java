package com.kodality.vacation.config;

import io.micronaut.context.annotation.Factory;
import jakarta.inject.Singleton;
import org.springframework.jdbc.core.JdbcTemplate;
import javax.sql.DataSource;

@Factory
public class JdbcConfig {

    @Singleton
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}