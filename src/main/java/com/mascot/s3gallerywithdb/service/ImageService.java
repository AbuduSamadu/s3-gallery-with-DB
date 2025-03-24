package com.mascot.s3gallerywithdb.service;

import com.mascot.s3gallerywithdb.respository.ImageRespository;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Service
public class ImageService {
    private final ImageRespository imageRespository;
    private final S3Service s3Service;

    private final Logger logger = LoggerFactory.getLogger(S3Service.class);


    public ImageService(ImageRespository imageRespository, S3Service s3Service) {
        this.imageRespository = imageRespository;
        this.s3Service = s3Service;
    }

    public void uploadImage(MultipartFile file, String name, String description) throws IOException {
        if (!file.isEmpty()) {
            logger.info("Uploading image " + name + " to " + description);
            throw new BadRequestException("Uploading image " + name + " to " + description);
        }


    }
}
