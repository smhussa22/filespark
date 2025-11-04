package com.filespark;

import java.io.File;
import java.util.UUID;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

public final class S3Uploader {

    private S3Uploader() {}
    private static S3Client s3;

    private static S3Client getClient() {

        if (s3 != null) return s3;

        String accessKey = Config.getS3AccessKey();
        String secretKey = Config.getS3SecretKey();

        if (accessKey == null || secretKey == null || accessKey.isEmpty() || secretKey.isEmpty()){

            throw new IllegalStateException("AWS Credentials Missing");

        }

        s3 = S3Client.builder()
        .region(Config.S3_REGION)
        .credentialsProvider(
            StaticCredentialsProvider.create(
                AwsBasicCredentials.create(accessKey, secretKey)
            )
        )
        .build();

        // debug
        System.out.println("s3 client initialized");
        return s3;

    }
    

    public static void uploadFile (File file) {

        try {

            String key = Config.S3_UPLOAD_STRING_KEY + UUID.randomUUID() + "_" + file.getName();
            PutObjectRequest request = PutObjectRequest.builder()
                .bucket(Config.S3_BUCKET_NAME)
                .key(key)
                .build();
            getClient().putObject(request, RequestBody.fromFile(file));
            System.out.println("Uploaded " + file.getName() + " to key " + key);

        }
        catch (Exception exception){

            System.err.println(exception.getMessage());
            exception.printStackTrace();

        }

    }

}
