package com.mascot.s3gallerywithdb.model;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "images")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",
            nullable = false)
    private Long Id;

    @Column(nullable = false)
    private String s3Url;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String ContentType;

    @Column(nullable = false)
    private Long size;

    @Column(nullable = false)
    private LocalDateTime createdAt;

}
