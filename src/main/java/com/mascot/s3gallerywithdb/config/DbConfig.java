package com.mascot.s3gallerywithdb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import software.amazon.awssdk.services.ssm.SsmClient;
import software.amazon.awssdk.services.ssm.model.GetParameterRequest;
import software.amazon.awssdk.services.ssm.model.GetParameterResponse;

import javax.sql.DataSource;

@Configuration
public class DbConfig {

    private final SsmClient ssmClient;

    public DbConfig(SsmClient ssmClient) {
        this.ssmClient = ssmClient;
    }


    @Bean
    public DataSource dataSource() {

        String dbEndpoint = getParameter("/gallery-app/rds/endpointt");
        String dbUsername = getParameter("/gallery-app/rds/username");
        String dbPassword = getParameter("/gallery-app/rds/password");

        // Configure the DataSource
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl(String.format("jdbc:postgresql://%s:5432/postgres", dbEndpoint));
        dataSource.setUsername(dbUsername);
        dataSource.setPassword(dbPassword);

        return dataSource;
    }

    private String getParameter(String parameterName) {
        GetParameterRequest request = GetParameterRequest.builder()
                .name(parameterName)
                .withDecryption(true) // Decrypt SecureString parameters
                .build();

        GetParameterResponse response = ssmClient.getParameter(request);
        return response.parameter().value();
    }


}