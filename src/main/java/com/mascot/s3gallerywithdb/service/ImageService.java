package com.mascot.s3gallerywithdb.service;

import com.mascot.s3gallerywithdb.exception.BadRequestException;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

@Service
public class ImageService {

    private static final Logger logger = LoggerFactory.getLogger(ImageService.class);

    private final ImageRespository imageRespository;
    private final S3Service s3Service;

    public ImageService(ImageRespository imageRespository, S3Service s3Service) {
        this.imageRespository = imageRespository;
        this.s3Service = s3Service;
    }


    public void uploadImage(MultipartFile file, String name, String description) {
        if (file == null || file.isEmpty()) {
            logger.error("File is empty or null");
            throw new BadRequestException("The uploaded file cannot be empty.");
        }

        try {
            String key = generateUniqueKey(name, getFileExtension(file.getOriginalFilename()));
            File tempFile = convertMultipartFileToFile(file);
            s3Service.uploadFile(key, tempFile);
            tempFile.delete();
            Image image = Image.builder()
                    .key(key)
                    .name(name)
                    .description(description)
                    .createdAt(LocalDateTime.now())
                    .build();

            imageRespository.save(image);

            logger.info("Successfully uploaded image with key: {}", key);
        } catch (IOException e) {
            logger.error("Failed to process the uploaded file", e);
            throw new InternalServerErrorException("An error occurred while processing the file.");
        }
    }

    public Page<Image> getAllImages(Pageable pageable) {
        return imageRespository.findAll(pageable);
    }

    public void deleteImage(String key) {
        if(!s3Service.fileExists(key)){
            logger.error("Image with key {} does not exist", key);
            throw new ResourceNotFoundException("Image with key " + key + " does not exist.");
        }
        try {
            s3Service.deleteImage(key);
            imageRespository.deleteById(key);
            logger.info("Successfully deleted image with key: {}", key);
        } catch (Exception e) {
            logger.error("Failed to delete image with key: {}", key, e);
            throw new InternalServerErrorException("An error occurred while deleting the image.");
        }
    }

    private File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
        Path tempFilePath = Files.createTempFile("upload-", "-" + multipartFile.getOriginalFilename());
        Files.write(tempFilePath, multipartFile.getBytes());
        return tempFilePath.toFile();
    }
    private String generateUniqueKey(String name, String extension) {
        String sanitizedFileName = name.replaceAll("[^a-zA-Z0-9.-]", "_");
        return sanitizedFileName + "-" + System.currentTimeMillis() + "." + extension;
    }

    private String getFileExtension(String originalFilename) {
        if (originalFilename == null || !originalFilename.contains(".")) {
            return "bin"; // Default extension if none is found
        }
        return originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
    }
}