package com.wbt.reviewmicroservice.review;

import com.wbt.reviewmicroservice.review.dto.ReviewRequest;
import com.wbt.reviewmicroservice.review.dto.ReviewResponse;

import java.util.List;
import java.util.Optional;

public interface ReviewService {
    List<ReviewResponse> fetchAll();

    Optional<ReviewResponse> fetchById(final Long reviewId);

    List<ReviewResponse> fetchByCompanyId(final Long companyId);

    Boolean add(final ReviewRequest request, final Long companyId);

    Boolean update(final Long reviewId, final ReviewRequest request, final Long company);

    Boolean delete(final Long reviewId);

    Boolean doExist(final Long reviewId);
}
