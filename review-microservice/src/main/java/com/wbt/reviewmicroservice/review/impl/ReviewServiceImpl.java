package com.wbt.reviewmicroservice.review.impl;

import com.wbt.reviewmicroservice.review.*;
import com.wbt.reviewmicroservice.review.dto.ReviewResponse;
import com.wbt.reviewmicroservice.review.util.EntityDtoConverter;
import com.wbt.reviewmicroservice.review.dto.ReviewRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository(value = "JPA")
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public List<ReviewResponse> fetchAll() {
        return this.reviewRepository
                .findAll()
                .stream()
                .map(EntityDtoConverter::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ReviewResponse> fetchById(final Long reviewId) {
        return this.reviewRepository
                .findById(reviewId)
                .map(EntityDtoConverter::toResponse);
    }

    @Override
    public List<ReviewResponse> fetchByCompanyId(final Long companyId) {
        return this.reviewRepository
                .findByCompanyId(companyId)
                .stream()
                .map(EntityDtoConverter::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Boolean add(final ReviewRequest request, final Long companyId) {
        this.reviewRepository.save(new Review(request.title(), request.content(), request.rating(), companyId));
        return true;
    }

    @Override
    public Boolean update(final Long reviewId, final ReviewRequest request, final Long companyId) {
        final var existingReview = this.fetchById(reviewId);
        if (existingReview.isPresent()) {
            final var review = existingReview.get();
            final var newReview = new Review(review.id(), request.title(), request.content(), request.rating(), review.createdAt(), companyId);
            this.reviewRepository.save(newReview);
            return true;
        }
        return false;
    }

    @Override
    public Boolean doExist(final Long reviewId) {
        return this.reviewRepository.existsById(reviewId);
    }

    @Override
    public Boolean delete(final Long reviewId) {
        if (this.reviewRepository.existsById(reviewId)) {
            this.reviewRepository.deleteById(reviewId);
            return true;
        }
        return false;
    }
}
