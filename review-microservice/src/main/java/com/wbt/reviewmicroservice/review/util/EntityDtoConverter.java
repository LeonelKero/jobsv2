package com.wbt.reviewmicroservice.review.util;

import com.wbt.reviewmicroservice.review.Review;
import com.wbt.reviewmicroservice.review.dto.ReviewResponse;

public record EntityDtoConverter() {

    public static ReviewResponse toResponse(final Review review) {
        return new ReviewResponse(
                review.getId(),
                review.getTitle(),
                review.getContent(),
                review.getCreatedAt(),
                review.getUpdatedAt(),
                review.getRating(),
                review.getCompanyId()
        );
    }

}
