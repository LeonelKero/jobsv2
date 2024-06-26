package com.wbt.companymicroservice.company;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CompanyRepositoryTest {

    @Container
    @ServiceConnection
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16");

    @Autowired
    private CompanyRepository underTest;

    @BeforeEach
    void setUp() {
        underTest.save(new Company("wbt", "Work always beat talent"));
    }

    @Test
    void postgresContainerIsWellConfigured() {
        assertThat(postgreSQLContainer.isCreated()).isTrue();
        assertThat(postgreSQLContainer.isRunning()).isTrue();
    }

    @Test
    void findByDescription() {
        // GIVEN // WHEN
        final var company = underTest.findByDescription("toto");
        // THEN
        assertThat(company).isEmpty();
    }

    @Test
    void whenCompanyExistsByNameReturnTrue() {
        // GIVEN
        final var company = new Company("wbt", "work beats talent");
        underTest.save(company);
        // WHEN
        final var actualResult = underTest.existsByName(company.getName());
        // THEN
        assertThat(actualResult).isTrue();
    }

    @Test
    void whenCompanyDoNotExistsByNameReturnFalse() {
        // GIVEN
        final var nonExisting = "company A";
        // WHEN
        final var actualResult = underTest.existsByName(nonExisting);
        // THEN
        assertThat(actualResult).isFalse();
    }
}