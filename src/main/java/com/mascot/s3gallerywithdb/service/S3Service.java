package com.mascot.s3gallerywithdb.service;


import com.mascot.s3gallerywithdb.exception.InternalServerErrorException;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;


@Service
public class S3Service {

    private static final String BUCKET_NAME = "image-gallery-bucket-one";

    private final S3Client s3Client;

    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public void uploadFile(String key, File file) {
        PutObjectRequest request = PutObjectRequest
                .builder()
                .bucket(BUCKET_NAME)
                .key(key)
                .build();
        s3Client.putObject(request, RequestBody.fromFile(file));
    }

    public void deleteImage(String key) {
        try {
            DeleteObjectRequest request = DeleteObjectRequest.builder()
                    .bucket(BUCKET_NAME)
                    .key(key)
                    .build();
            s3Client.deleteObject(request);

        } catch (Exception e) {
            throw new InternalServerErrorException("Failed to delete Image ");
        }
    }

    public String generateImageUrl(String key) {
        String s3Url = "https://" + BUCKET_NAME + ".s3.amazonaws.com/" + key;
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(key)
                .build();
        s3Client.getObject(request);
        return s3Url;
    }


}