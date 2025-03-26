package com.mascot.s3gallerywithdb.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.Index;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "images")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private String key;

    @Column(nullable = false, columnDefinition = "TEXT")
    @NotBlank(message = "S3 URL is required")
    @Size(max = 2048, message = "S3 URL must not exceed 2048 characters")
    private String s3Url;

    @Column(nullable = false, columnDefinition = "TEXT")
    @NotBlank(message = "Description is required")
    @Size(max = 4096, message = "Description must not exceed 4096 characters")
    private String description;

    @Column(nullable = false, length = 512)
    @NotBlank(message = "Name is required")
    @Size(max = 512, message = "Name must not exceed 512 characters")
    @Index(name = "idx_image_name")
    private String name;

    @Column(nullable = false, length = 255)
    @NotBlank(message = "Content Type is required")
    @Size(max = 255, message = "Content Type must not exceed 255 characters")
    @Index(name = "idx_image_content_type")
    private String contentType;

    @Column(nullable = false)
    @NotNull(message = "Size is required")
    private Long size;


    @Column(nullable = false)
    private LocalDateTime createdAt;


}