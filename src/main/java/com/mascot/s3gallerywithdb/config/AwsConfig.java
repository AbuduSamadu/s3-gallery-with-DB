package com.mascot.s3gallerywithdb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.ssm.SsmClient;

@Configuration
public class AwsConfig {

   @Bean
   public SsmClient ssmClient() {
       return SsmClient.builder()
               .region(Region.US_EAST_1)
               .credentialsProvider(DefaultCredentialsProvider.create())
               .build();
   }

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }
    @Bean
    public S3Presigner s3Presigner(){
        return S3Presigner.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }
}
