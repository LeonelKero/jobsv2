package com.wbt.reviewmicroservice.review;

import com.wbt.reviewmicroservice.BaseTestContainersUnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ReviewRepositoryTest extends BaseTestContainersUnitTest {
    @Autowired
    private ReviewRepository underTest;

    @BeforeEach
    void setUp() {
        underTest.deleteAll(); // start each test with clean database
    }

    @Test
    void findByCompanyId() {
        // GIVEN
        final Long companyId = 1L;
        final var review = new Review("Warm welcome", "Nice people here", 4.5, companyId);
        final var savedReview = underTest.save(review);
        // WHEN
        final var actualResponse = underTest.findByCompanyId(companyId);
        // THEN
        assertThat(actualResponse.size()).isGreaterThan(0);
        assertThat(actualResponse.get(0).getContent()).isEqualTo(savedReview.getContent());
    }

    @Test
    void whenThereIsNoReviewForCompanyIdReturnEmptyList() {
        // GIVEN
        final var nonExistingCompanyId = 10L;
        // WHEN
        final var actualResponse = underTest.findByCompanyId(nonExistingCompanyId);
        // THEN
        assertThat(actualResponse.size()).isEqualTo(0);
    }

    @Test
    void findByCreatedAtOrderByIdDesc() {
        // GIVEN
        final var review = new Review("Review 01", "Review content 01", 5., 1L);
        final var savedReview = underTest.save(review);
        System.out.println(savedReview.getCreatedAt());
        // WHEN
        final var actualResponse = underTest.findByCreatedAtOrderByIdDesc(savedReview.getCreatedAt());
        // THEN
        assertThat(actualResponse.size()).isGreaterThan(0);
        assertThat(actualResponse.get(0).getId()).isNotNull();
    }

    @Test
    void whenThereIsNoReviewForCreationDateTimeReturnEmptyList() {
        // GIVEN
        final var someDateTime = LocalDateTime.of(2036, 12, 25, 17, 0);
        // WHEN
        final var actualResponse = underTest.findByCreatedAtOrderByIdDesc(someDateTime);
        // THEN
        assertThat(actualResponse.size()).isEqualTo(0);
    }
}