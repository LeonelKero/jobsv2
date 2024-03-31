package com.wbt.companymicroservice.company;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

        // WHEN

        // THEN
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