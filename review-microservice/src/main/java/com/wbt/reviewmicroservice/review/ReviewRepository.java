package com.wbt.reviewmicroservice.review;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByCompanyId(final Long id);
    List<Review> findByCreatedAtOrderByIdDesc(final LocalDateTime creationDate);
}
