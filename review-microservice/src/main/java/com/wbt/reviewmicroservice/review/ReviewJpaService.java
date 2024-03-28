package com.wbt.reviewmicroservice.review;

import com.wbt.reviewmicroservice.review.dto.ReviewRequest;
import com.wbt.reviewmicroservice.review.dto.ReviewResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public record ReviewJpaService(ReviewService reviewService) {

    public List<ReviewResponse> fetchAll() {
        return this.reviewService.fetchAll();
    }

    public List<ReviewResponse> fetchCompanyReviews(final Long id) {
        return this.reviewService.fetchByCompanyId(id);
    }

    public Boolean addReview(final ReviewRequest reviewRequest, final Long companyId) {
        return this.reviewService.add(reviewRequest, companyId);
    }

    public ReviewResponse fetchById(final Long id) {
        return this.reviewService.fetchById(id).orElse(null);
    }

    public Boolean update(final Long rId, final ReviewRequest reviewRequest, final Long cId) {
        return this.reviewService.update(rId, reviewRequest, cId);
    }

    public Boolean remove(final Long id) {
        return this.reviewService.delete(id);
    }

}
