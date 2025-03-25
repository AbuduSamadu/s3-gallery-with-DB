package com.mascot.s3gallerywithdb.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "images")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",
            nullable = false)
    private String key;

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
