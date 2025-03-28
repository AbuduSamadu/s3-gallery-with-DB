package com.mascot.s3gallerywithdb.config;

import io.github.cdimascio.dotenv.Dotenv;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
public class DbConfig {

    @Value("${application.db-url}")
    private String dbUrl;
    @Value("${application.db-url-username}")
    private String dbUsername;
    @Value("${application.db-url-password}")
    private String dbPassword;

    @Bean
    public Dotenv dotenv() {
        return Dotenv.load();
    }

    @Bean
    @Profile("prod")
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .url(dbUrl)
                .username(dbUsername)
                .password(dbPassword)
                .driverClassName("org.postgresql.Driver")
                .build();
    }
}