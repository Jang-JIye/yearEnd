package com.future.yearend.photo;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface S3Repository extends JpaRepository<Photo, Long> {
    List<Photo> findTop12ByOrderByCreatedAtDesc(PageRequest of);
}
