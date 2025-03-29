package com.mascot.s3gallerywithdb.service;


import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.File;
import java.time.Duration;

@Service
public class S3Service {

    private static final String BUCKET_NAME = "image-gallery-bucket-new";

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    public S3Service(S3Client s3Client, S3Presigner s3Presigner) {
        this.s3Client = s3Client;
        this.s3Presigner = s3Presigner;
    }

    public void uploadFile(String key, File file) {
        PutObjectRequest request = PutObjectRequest
                .builder()
                .bucket(BUCKET_NAME)
                .key(key)
                .build();
        s3Client.putObject(request, RequestBody.fromFile(file));
    }


    public String generatePresidedUrl(String key) {
        GetObjectRequest getObjectRequest = GetObjectRequest
                .builder()
                .bucket(BUCKET_NAME)
                .key(key)
                .build();

        PresignedGetObjectRequest resignedRequest = s3Presigner
                .presignGetObject(r -> r
                        .getObjectRequest(getObjectRequest)
                        .signatureDuration(Duration.ofMinutes(120)));
        return resignedRequest.url().toString();
    }


}