package com.wbt.companymicroservice.company;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompanyServiceTest {

    private CompanyService underTest;

    @Mock
    private CompanyRepository companyRepository;

    @BeforeEach
    void setUp() {
        underTest = new CompanyService(companyRepository);
    }

    @Test
    void fetchAll() {
        // GIVEN
        when(companyRepository.findAll()).thenReturn(List.of(
                new Company(1L, "wbt", "work beats talent")
        ));
        // WHEN
        final var actualResponse = underTest.fetchAll();
        // THEN
        verify(companyRepository).findAll();
        assertThat(actualResponse.size()).isGreaterThan(0);
    }

    @Test
    void findById() {
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
    void delete() {
        // GIVEN

        // WHEN

        // THEN
    }
}