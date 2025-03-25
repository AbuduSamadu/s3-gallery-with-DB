package com.mascot.s3gallerywithdb.controller;

import com.mascot.s3gallerywithdb.exception.BadRequestException;
import com.mascot.s3gallerywithdb.exception.InternalServerErrorException;
import com.mascot.s3gallerywithdb.model.Image;
import com.mascot.s3gallerywithdb.service.ImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/images")
public class ImageController {

    private static final Logger logger = LoggerFactory.getLogger(ImageController.class);

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }


    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("name") String name,
            @RequestParam("description") String description) {
        try {
            imageService.uploadImage(file, name, description);
            return ResponseEntity.status(HttpStatus.CREATED).body("Image uploaded successfully.");
        } catch (BadRequestException e) {
            logger.error("Bad request: {}", e.getMessage());
            throw new BadRequestException("Bad request: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<Page<Image>> getAllImages(Pageable pageable) {
        try {
            Page<Image> images = imageService.getAllImages(pageable);
            return ResponseEntity.ok(images);
        } catch (Exception e) {
            logger.error("Error while fetching images: {}", e.getMessage());
            throw new InternalServerErrorException("Error while fetching images: " + e.getMessage());
        }
    }

    @DeleteMapping("/{key}")
    public ResponseEntity<String> deleteImage(@PathVariable String key) {
        try {
            imageService.deleteImage(key);
            return ResponseEntity.ok("Image deleted successfully.");
        } catch (InternalServerErrorException e) {
            logger.error("Error while deleting image with key {}: {}", key, e.getMessage());
            throw new InternalServerErrorException("Error while deleting image with key: " + key);
        }
    }
}