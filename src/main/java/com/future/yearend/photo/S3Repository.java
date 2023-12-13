package com.future.yearend.photo;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface S3Repository extends JpaRepository<Photo, Long> {
    List<Photo> findTop12ByOrderByCreatedAtDesc(PageRequest of);

    List<Photo> findAllByMonth(String month);

    List<Photo> findLatestPhotosByMonth(@Param("month") String month);
}
