package com.mascot.s3gallerywithdb.controller;

import com.mascot.s3gallerywithdb.exception.InternalServerErrorException;
import com.mascot.s3gallerywithdb.model.Image;
import com.mascot.s3gallerywithdb.service.ImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
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
            @RequestParam("imageName") String imageName,
            @RequestParam("imageDescription") String imageDescription) {

        imageService.uploadImage(file, imageName, imageDescription);
        return ResponseEntity.status(HttpStatus.CREATED).body("Image uploaded successfully.");

    }

    @GetMapping("/gallery")
    public ResponseEntity<Page<Image>> listImagesApi(Pageable pageable) {
        Page<Image> images = imageService.listImages(pageable);
        return ResponseEntity.ok(images);
    }

    @GetMapping
    public String getGalleryPage(Model model, Pageable pageable) {
        Page<Image> images = imageService.listImages(pageable);
        model.addAttribute("content", images.getContent());
        model.addAttribute("pageNumber", images.getNumber());
        model.addAttribute("pageSize", images.getSize());
        model.addAttribute("totalElements", images.getTotalElements());
        model.addAttribute("totalPages", images.getTotalPages());
        model.addAttribute("last", images.isLast());
        return "index"; // Name of the Thymeleaf template
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