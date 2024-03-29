package com.wbt.reviewmicroservice.review.impl;

import com.wbt.reviewmicroservice.review.Review;
import com.wbt.reviewmicroservice.review.ReviewRepository;
import com.wbt.reviewmicroservice.review.ReviewService;
import com.wbt.reviewmicroservice.review.dto.ReviewRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(value = MockitoExtension.class)
class ReviewServiceImplTest {
    @Mock
    private ReviewRepository reviewRepository;
    private ReviewService underTest;

    @BeforeEach
    void setUp() {
        underTest = new ReviewServiceImpl(reviewRepository);
    }

    @Test
    void fetchAll() {
        // GIVEN // WHEN
        underTest.fetchAll();
        // THEN
        verify(reviewRepository).findAll();
    }

    @Test
    void fetchById() {
        // GIVEN
        final var someReviewId = 4L;
        // WHEN
        underTest.fetchById(someReviewId);
        // THEN
        verify(reviewRepository).findById(someReviewId);
    }

    @Test
    void fetchByCompanyId() {
        // GIVEN
        final var someCompanyId = 3L;
        // WHEN
        underTest.fetchByCompanyId(someCompanyId);
        // THEN
        verify(reviewRepository).findByCompanyId(someCompanyId);
    }

    @Test
    void add() {
        // GIVEN
        final var company = 1L;
        final var reviewRequest = new ReviewRequest(
                "Awesome jobs",
                "Really nice and well paid jobs in this company",
                4.7);
        // WHEN
        underTest.add(reviewRequest, company);
        final var argumentCaptor = ArgumentCaptor.forClass(Review.class);
        // THEN
        // capture the argument
        verify(reviewRepository).save(argumentCaptor.capture());
        final var capturedReview = argumentCaptor.getValue();

        assertThat(capturedReview.getId()).isNull();
        assertThat(capturedReview.getTitle()).isEqualTo(reviewRequest.title());
        assertThat(capturedReview.getContent()).isEqualTo(reviewRequest.content());
        assertThat(capturedReview.getRating()).isEqualTo(reviewRequest.rating());
        assertThat(capturedReview.getCompanyId()).isEqualTo(company);
    }

    @Test
    void update() {
        // GIVEN
        final var existingReviewId = 1L;
        final var existingCompanyId = 1L;
        final var review = new Review(existingReviewId,
                "Test review", "Test review content",
                5.,
                3L);
        when(reviewRepository.findById(existingReviewId)).thenReturn(Optional.of(review));
        final var requestUpdate = new ReviewRequest(
                "Test review updated",
                "Test review content",
                4.);
        // WHEN
        final var argumentCaptor = ArgumentCaptor.forClass(Review.class);
        underTest.update(existingReviewId, requestUpdate, existingCompanyId);
        // THEN
        verify(reviewRepository).save(argumentCaptor.capture());
        final var capturedreview = argumentCaptor.getValue();

        assertThat(capturedreview.getId()).isEqualTo(review.getId());
        assertThat(capturedreview.getTitle()).isEqualTo(requestUpdate.title());
        assertThat(capturedreview.getCompanyId()).isEqualTo(existingCompanyId);
        assertThat(capturedreview.getContent()).isEqualTo(requestUpdate.content());
        assertThat(capturedreview.getRating()).isEqualTo(requestUpdate.rating());
    }

    @Test
    void whenReviewDoNotExistNoPossibleUpdate() {
        // GIVEN
        final var nonExistingReviewId = -1L;
        when(reviewRepository.findById(nonExistingReviewId)).thenReturn(Optional.empty());
        final var requestForUpdate = new ReviewRequest("Test review updated", "Test review content", 4.);
        // WHEN
        underTest.update(nonExistingReviewId, requestForUpdate, 1L);
        // THEN
        verify(reviewRepository, never()).save(any(Review.class));
    }

    @Test
    void doExist() {
        // GIVEN
        final var reviewId = 1L;
        when(reviewRepository.existsById(reviewId)).thenReturn(true);
        // WHEN
        final var actualResponse = underTest.doExist(reviewId);
        // THEN
        verify(reviewRepository).existsById(reviewId);
        assertThat(actualResponse).isTrue();
    }

    @Test
    void whenReviewDoNotExistsReturnFalse() {
        // GIVEN
        final var nonExistingReviewId = -2L;
        when(reviewRepository.existsById(nonExistingReviewId)).thenReturn(false);
        // WHEN
        final var actualResponse = underTest.doExist(nonExistingReviewId);
        // THEN
        verify(reviewRepository).existsById(nonExistingReviewId);
        assertThat(actualResponse).isFalse();
    }

    @Test
    void delete() {
        // GIVEN
        final var reviewId = 1L;
        when(reviewRepository.existsById(reviewId)).thenReturn(true);
        // WHEN
        final var actualResponse = underTest.delete(reviewId);
        // THEN
        verify(reviewRepository).deleteById(reviewId);
        assertThat(actualResponse).isTrue();
    }

    @Test
    void whenReviewDoNotExistUnableToDeleteAndReturnFalse() {
        // GIVEN
        final var reviewId = -1L;
        when(reviewRepository.existsById(reviewId)).thenReturn(false);
        // WHEN
        final var actualResponse = underTest.delete(reviewId);
        // THEN
        verify(reviewRepository, never()).deleteById(reviewId);
        assertThat(actualResponse).isFalse();
    }
}