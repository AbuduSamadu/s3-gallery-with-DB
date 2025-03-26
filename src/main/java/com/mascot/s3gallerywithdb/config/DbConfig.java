package com.mascot.s3gallerywithdb.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.ssm.SsmClient;
import software.amazon.awssdk.services.ssm.model.GetParameterRequest;

import javax.sql.DataSource;

@Configuration
public class DbConfig {

    @Bean
    public DataSource dataSource(SsmClient ssmClient) {
        String dbHost = getParameter(ssmClient, "/s3-app/db-host");
        String dbPort = getParameter(ssmClient, "/s3-app/db-port");
        String dbName = getParameter(ssmClient, "/s3-app/db-name");
        String dbUsername = getParameter(ssmClient, "/s3-app/db-username");
        String dbPassword = getParameter(ssmClient, "/s3-app/db-password");

        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:postgresql://" + dbHost + ":" + dbPort + "/" + dbName);
        dataSource.setUsername(dbUsername);
        dataSource.setPassword(dbPassword);
        return dataSource;
    }

    private String getParameter(SsmClient ssmClient, String parameterName) {
        GetParameterRequest request = GetParameterRequest.builder()
                .name(parameterName)
                .withDecryption(true)
                .build();
        return ssmClient.getParameter(request).parameter().value();
    }
}