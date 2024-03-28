package com.wbt.reviewmicroservice.review.dto;

import java.time.LocalDateTime;

public record ReviewResponse(Long id, String title, String content, LocalDateTime createdAt, LocalDateTime updatedAt, Double rating, Long companyId) {
}
