package com.filespark.server.services.aws;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.regions.Region;

@Configuration
public class Amazon {

    @Bean
    public S3AsyncClient s3AsyncClient(@Value("${spring.data.aws.key.id}") String accessKey, @Value("${spring.data.aws.key.secret}") String secretKey, @Value("${spring.data.aws.region}") String region) {
        
        return S3AsyncClient.builder().region(Region.of(region)).credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey))).build();

    }
    
    @Bean
    public S3Presigner s3Presigner(@Value("${spring.data.aws.key.id}") String accessKey, @Value("${spring.data.aws.key.secret}") String secretKey, @Value("${spring.data.aws.region}") String region){

        return S3Presigner.builder().region(Region.of(region)).credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey))).build();

    }

}
