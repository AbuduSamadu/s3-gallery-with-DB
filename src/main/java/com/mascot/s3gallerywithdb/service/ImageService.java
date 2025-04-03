package com.mascot.s3gallerywithdb.service;

import com.mascot.s3gallerywithdb.exception.InternalServerErrorException;
import com.mascot.s3gallerywithdb.exception.ResourceNotFoundException;
import com.mascot.s3gallerywithdb.model.Image;
import com.mascot.s3gallerywithdb.respository.ImageRespository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class ImageService {

    private static final Logger logger = LoggerFactory.getLogger(ImageService.class);

    private final ImageRespository imageRespository;
    private final S3Service s3Service;

    public ImageService(ImageRespository imageRespository, S3Service s3Service) {
        this.imageRespository = imageRespository;
        this.s3Service = s3Service;
    }


    public void uploadImage(MultipartFile file, String imageName, String imageDescription) {
        if (!Objects.requireNonNull(file.getContentType()).startsWith("image/")) {
            logger.error("Only image files are allowed.");
            throw new IllegalArgumentException("Only image files are allowed.");
        }
        try {
            File tempFile = convertMultipartFileToFile(file);
            String objectKey = UUID.randomUUID().toString();
            s3Service.uploadFile(objectKey, tempFile);

            String url = s3Service.generateImageUrl(objectKey);
            String contentType = file.getContentType();
            long  size = file.getSize();

            Image image = Image.builder()
                    .key(objectKey)
                    .description(imageDescription)
                    .name(imageName)
                    .size(size)
                    .url(url)
                    .contentType(contentType)
                    .createdAt(LocalDateTime.now())
                    .build();

            logger.info("image and description have been uploaded: {} and:  {} ", imageName, imageDescription);

            imageRespository.save(image);
            logger.info("Successfully uploaded image with key: {}", image);
        } catch (IOException e) {
            logger.error("Failed to process the uploaded file", e);
            throw new InternalServerErrorException("An error occurred while processing the file.");
        }
    }

    public Page<Image> listImages(Pageable pageable) {
        // Fetch paginated results from the database
        Page<Image> imagePage = imageRespository.findAll(pageable);

        // Log for debugging purposes
        logger.info("Listing images with page {} and size {}. Total elements: {}, Total pages: {}",
                pageable.getPageNumber(),
                pageable.getPageSize(),
                imagePage.getTotalElements(),
                imagePage.getTotalPages());

        return imagePage;
    }

    public void deleteImage(String key) {
        Optional<Image> imageOptional = imageRespository.findById(key);
        if(imageOptional.isEmpty()){
            throw new ResourceNotFoundException("Image with the " + key +  " not found "  );
        }
        try {
            imageRespository.deleteById(key);
            s3Service.deleteImage(key);
            logger.info("Successfully deleted image with key: {}", key);
        } catch (Exception e) {
            logger.error("Failed to delete image with key: {}", key, e);
            throw new InternalServerErrorException("An error occurred while deleting the image.");
        }
    }

    private File convertMultipartFileToFile(MultipartFile file) throws IOException {
        File tempFile = File.createTempFile("temp", null);
        file.transferTo(tempFile);
        return tempFile;
    }


}