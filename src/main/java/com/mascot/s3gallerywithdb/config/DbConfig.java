package com.mascot.s3gallerywithdb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.ssm.SsmClient;
import software.amazon.awssdk.services.ssm.model.GetParameterRequest;
import software.amazon.awssdk.services.ssm.model.GetParameterResponse;

@Configuration
public class DbConfig {
    private static final String DB_USERNAME = "/s3-app/db-username";
    private static final String DB_PASSWORD = "/s3-app/db-password";

    private final SsmClient ssmClient;

    public DbConfig(SsmClient ssmClient) {
        this.ssmClient = ssmClient;
    }

    @Bean
    public String getDbUsername() {
        GetParameterRequest usernameRequest = GetParameterRequest
                .builder()
                .name(DB_USERNAME)
                .withDecryption(true)
                .build();
        GetParameterResponse usernameResponse = ssmClient.getParameter(usernameRequest);
        return usernameResponse.parameter().value();
    }

    @Bean
    public String getDbPassword() {
        GetParameterRequest passwordRequest = GetParameterRequest
                .builder()
                .name(DB_PASSWORD)
                .withDecryption(true)
                .build();
        GetParameterResponse passwordResponse = ssmClient.getParameter(passwordRequest);
        return passwordResponse.parameter().value();
    }

}
