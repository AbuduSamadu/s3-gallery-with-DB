package com.mascot.s3gallerywithdb.config;

import com.zaxxer.hikari.HikariDataSource;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.services.ssm.SsmClient;
import software.amazon.awssdk.services.ssm.model.GetParameterRequest;

import javax.sql.DataSource;

@Configuration
public class DbConfig {

    @Bean
    public Dotenv dotenv() {
        return Dotenv.load();
    }


    @Bean
    @Profile("prod")
    public DataSource dataSource(SsmClient ssmClient) {
        String dbUrl = getParameter(ssmClient, "/gallery/db/url");
        String dbUsername = getParameter(ssmClient, "/gallery/db/username");
        String dbPassword = getParameter(ssmClient, "/gallery/db/password");

        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(dbUrl);
        dataSource.setUsername(dbUsername);
        dataSource.setPassword(dbPassword);
        return dataSource;
    }

    private String getParameter(SsmClient ssmClient, String parameterName) {
        GetParameterRequest request = GetParameterRequest.builder().name(parameterName).withDecryption(true).build();
        return ssmClient.getParameter(request).parameter().value();
    }
}