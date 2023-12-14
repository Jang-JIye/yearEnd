package com.future.yearend.saying;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SayingRepository extends JpaRepository<Saying, Long> {
    @Query(value = "SELECT * FROM sayings ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Saying findRandomSaying();

    Optional<Saying> findById(Long id);
}
