package com.wbt.companymicroservice.company;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class CompanyControllerTest {

    @Container
    @ServiceConnection
    private final static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16");

    private static final String URI = "/api/v1/companies";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CompanyRepository companyRepository;

    @Test
    void connectionEstablished() {
        assertThat(postgreSQLContainer.isRunning()).isTrue();
        assertThat(postgreSQLContainer.isCreated()).isTrue();
    }

    @BeforeEach
    void setUp() {
        companyRepository.deleteAll();
    }

    @Test
    void companies() {
        // GIVEN // WHEN
        final var responses = restTemplate.getForObject(URI, CompanyResponse[].class);
        // THEN
        assertThat(responses).isEmpty();
    }

    @Test
    void companyByIdReturnBadRequestWhenNotExists() {
        // GIVEN // WHEN
        final var response = restTemplate.exchange(
                URI.concat("/1"),
                HttpMethod.GET,
                null,
                CompanyResponse.class
        );
        // THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void whenCompanyExistsFindingByIdReturnAResponse() {
        // GIVEN -> save new company
        final var request = new CompanyRequest("wbt", "work beats talent");
        restTemplate.exchange(
                URI,
                HttpMethod.POST,
                new HttpEntity<>(request),
                String.class
        );
        // WHEN -> get that company by id
        final var response = restTemplate.exchange(
                URI.concat("/1"),
                HttpMethod.GET,
                null,
                CompanyResponse.class
        );
        // THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().id()).isNotNull();
        assertThat(response.getBody().name()).isEqualTo(request.name());
        assertThat(response.getBody().description()).isEqualTo(request.description());
    }

    @Test
    void create() {
        // GIVEN
        final var request = new CompanyRequest("wbt", "work beats talent");
        // WHEN
        final var response = restTemplate.exchange(
                URI,
                HttpMethod.POST,
                new HttpEntity<>(request),
                String.class
        );
        // THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo("Company resource created successfully");
    }

    @Test
    void update() {
        // GIVEN
        final var newCompanyRequest = new CompanyRequest("wbt", "work beats talent");
        restTemplate
                .exchange(
                        URI,
                        HttpMethod.POST,
                        new HttpEntity<>(newCompanyRequest),
                        String.class);
        final var existingCompany = restTemplate.getForObject(URI, CompanyResponse[].class);
        // WHEN
        final var updateRequest = new CompanyRequest("wbt", "work always beats talent");
        final var existingId = existingCompany[0].id();
        final var updateResponse = restTemplate
                .exchange(URI.concat("/{id}"),
                        HttpMethod.PUT,
                        new HttpEntity<>(updateRequest),
                        String.class,
                        existingId
                );
        // THEN
        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void couldNotUpdateNonExistingCompany() {
        // GIVEN
        final var fakeId = -3L;
        final var requestUpdate = new CompanyRequest("ben", "bendo");
        // WHEN
        final var response = restTemplate.exchange(
                URI.concat("/{id}"),
                HttpMethod.PUT,
                new HttpEntity<>(requestUpdate),
                String.class,
                fakeId);
        // THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void delete() {
        // GIVEN
        final var newCompanyRequest = new CompanyRequest("wbt", "work beats talent");
        restTemplate
                .exchange(
                        URI,
                        HttpMethod.POST,
                        new HttpEntity<>(newCompanyRequest),
                        String.class);
        final var companies = restTemplate.getForObject(URI, CompanyResponse[].class);
        // WHEN
        final var response = restTemplate.exchange(
                URI.concat("/{id}"),
                HttpMethod.DELETE,
                null,
                String.class,
                companies[0].id());

        // THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Company resource successfully removed");
    }

    @Test
    void whenTryingToRemoveNonExistingCompanyGetBadRequest() {
        // GIVEN
        final var nonExistingCompany = -1L;
        // WHEN
        final var response = restTemplate
                .exchange(
                        URI.concat("/{id}"),
                        HttpMethod.DELETE,
                        null,
                        String.class,
                        nonExistingCompany);
        // THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}