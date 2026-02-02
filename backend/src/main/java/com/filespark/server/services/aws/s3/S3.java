package com.filespark.server.services.aws.s3;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@Service
public class S3 {
    
    private final S3AsyncClient s3;
    private final S3Presigner presigner;
    private final String bucket;

    public S3(S3AsyncClient s3, S3Presigner presigner, @Value("${spring.data.aws.bucket}") String bucket) {

        this.s3 = s3;
        this.presigner = presigner;
        this.bucket = bucket;

    }

    public String generatePresignedGetUrl(String key, long expiresSeconds){

        GetObjectRequest request = GetObjectRequest.builder().bucket(bucket).key(key).build();
        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder().getObjectRequest(request).signatureDuration(Duration.ofSeconds(expiresSeconds)).build();

        return presigner.presignGetObject(presignRequest).url().toString();

    }

    public String generatePresignedPutUrl(String key, String mime, long expiresSeconds){

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder().putObjectRequest(builder -> builder.bucket(bucket).key(key).contentType(mime)).signatureDuration(Duration.ofSeconds(expiresSeconds)).build();
        return presigner.presignPutObject(presignRequest).url().toString();

    }

    public CompletableFuture<String> getMimeType(String key){

        return s3.headObject(HeadObjectRequest.builder().bucket(bucket).key(key).build()).thenApply(response -> response.contentType());

    }

}
