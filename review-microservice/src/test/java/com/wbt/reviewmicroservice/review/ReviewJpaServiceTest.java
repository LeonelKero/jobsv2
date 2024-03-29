package com.wbt.reviewmicroservice.review;

import com.wbt.reviewmicroservice.review.dto.ReviewRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(value = MockitoExtension.class)
class ReviewJpaServiceTest {
    private ReviewJpaService underTest;
    @Mock
    private ReviewService reviewService;

    @BeforeEach
    void setUp() {
        underTest = new ReviewJpaService(reviewService);
    }

    @Test
    void fetchAll() {
        // GIVEN // WHEN
        underTest.fetchAll();
        // THEN
        verify(reviewService).fetchAll();
    }

    @Test
    void fetchCompanyReviews() {
        // GIVEN
        final var someCompanyId = 2L;
        // WHEN
        underTest.fetchCompanyReviews(someCompanyId);
        // THEN
        verify(reviewService).fetchByCompanyId(someCompanyId);
    }

    @Test
    void addReview() {
        // GIVEN
        final var someCompanyId = 1L;
        final var newReviewRequest = new ReviewRequest("New review", "New review content", 4.3);
        // WHEN
        underTest.addReview(newReviewRequest, someCompanyId);
        // THEN
        verify(reviewService).add(newReviewRequest, someCompanyId);
    }

    @Test
    void fetchById() {
        // GIVEN
        final var existingReviewId = 1L;
        // WHEN
        underTest.fetchById(existingReviewId);
        // THEN
        verify(reviewService).fetchById(existingReviewId);
    }

    @Test
    void update() {
        // GIVEN
        final var reviewId = 1L;
        final var companyId = 1L;
        final var requestForUpdate = new ReviewRequest("Title request", "Content request", 3.);
        // WHEN
        underTest.update(reviewId, requestForUpdate, companyId);
        // THEN
        verify(reviewService).update(reviewId, requestForUpdate, companyId);
    }

    @Test
    void remove() {
        // GIVEN
        final var reviewId = 9L;
        // WHEN
        underTest.remove(reviewId);
        // THEN
        verify(reviewService).delete(reviewId);
    }
}