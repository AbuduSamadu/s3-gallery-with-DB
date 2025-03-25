package com.mascot.s3gallerywithdb.respository;

import com.mascot.s3gallerywithdb.model.Image;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRespository extends JpaRepository<Image, String> {
    Page<Image> findAll(Pageable pageable);
}
