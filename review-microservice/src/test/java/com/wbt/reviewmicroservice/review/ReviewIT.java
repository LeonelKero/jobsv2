package com.wbt.reviewmicroservice.review;

import com.wbt.reviewmicroservice.review.dto.ReviewRequest;
import com.wbt.reviewmicroservice.review.dto.ReviewResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReviewIT {

    private static final String URI = "/api/v1/reviews";
    @Autowired
    private WebTestClient webTestClient;

    @Test
    void allReviews() {
        // GIVEN // WHEN
        final var response = webTestClient
                .get()
                .uri(URI.concat("/all"))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(new ParameterizedTypeReference<ReviewResponse>() {
                })
                .returnResult()
                .getResponseBody();
        // THEN
        assertThat(response).isNotNull();
        assertThat(response).isEmpty();
    }

    @Test
    void companyReviews() {
        // GIVEN -> a saved review in database for a company
        final var requestReview = new ReviewRequest("New review", "Content review", 4.);
        final var company = 1L;
        webTestClient
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path(URI)
                        .queryParam("company", company)
                        .build())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(requestReview), ReviewRequest.class)
                .exchange()
                .expectStatus().isCreated();
        // WHEN -> we get company's reviews
        final var response = webTestClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(URI)
                        .queryParam("company", company)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(new ParameterizedTypeReference<ReviewResponse>() {
                })
                .returnResult()
                .getResponseBody();
        // THEN -> check these
        assertThat(response).isNotEmpty();
        assertThat(response.get(0).title()).isEqualTo(requestReview.title());
        assertThat(response.get(0).content()).isEqualTo(requestReview.content());
        assertThat(response.get(0).rating()).isEqualTo(requestReview.rating());
    }

    @Test
    void getReview() {
        // GIVEN

        // WHEN

        // THEN
    }

    @Test
    void create() {
        // GIVEN

        // WHEN

        // THEN
    }

    @Test
    void update() {
        // GIVEN

        // WHEN

        // THEN
    }

    @Test
    void remove() {
        // GIVEN

        // WHEN

        // THEN
    }

    @Test
    void reviewJpaService() {
        // GIVEN

        // WHEN

        // THEN
    }
}