package com.wbt.companymicroservice.company;

import com.wbt.companymicroservice.exception.CompanyAlreadyExistException;
import com.wbt.companymicroservice.exception.CompanyNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
        final Long existingCompanyId = 1L;
        when(companyRepository.findById(existingCompanyId)).thenReturn(Optional.of(
                new Company(1L, "wbt", "work beats talent")));
        // WHEN
        final var actualResponse = underTest.findById(existingCompanyId);
        // THEN
        verify(companyRepository).findById(existingCompanyId);
        assertThat(actualResponse).isPresent();
    }

    @Test
    void findByDescription() {
        // GIVEN
        final var someDescription = "Nice company";
        when(companyRepository.findByDescription(someDescription))
                .thenReturn(List.of(new Company(1L, "wbt", "Nice company")));
        // WHEN
        final var actualResponse = underTest.findByDescription(someDescription);
        // THEN
        verify(companyRepository).findByDescription(someDescription);
        assertThat(actualResponse).isNotEmpty();
    }

    @Test
    void create() {
        // GIVEN
        final var request = new CompanyRequest("wbt", "work beats talent");
        when(companyRepository.existsByName(request.name())).thenReturn(false);
        final var argumentCaptor = ArgumentCaptor.forClass(Company.class);
        // WHEN
        final var actualResponse = underTest.create(request);
        // THEN
        verify(companyRepository).save(argumentCaptor.capture());
        final var capturedValue = argumentCaptor.getValue();

        assertThat(actualResponse).isTrue();
        assertThat(capturedValue.getId()).isNull();
        assertThat(capturedValue.getName()).isEqualTo(request.name());
        assertThat(capturedValue.getDescription()).isEqualTo(request.description());
    }

    @Test
    void createCompanyWhenNameAlreadyTakenThrowException() {
        // GIVEN
        final var existingCompanyName = "wbt";
        final var request = new CompanyRequest("wbt", "work beats talent");
        when(companyRepository.existsByName(existingCompanyName)).thenReturn(true);
        // WHEN
        assertThatThrownBy(() -> underTest.create(request))
                .isInstanceOf(CompanyAlreadyExistException.class)
                .hasMessage("Company name %s already taken".formatted(existingCompanyName));
        // THEN
        verify(companyRepository, never()).save(any(Company.class));
    }

    @Test
    void update() {
        // GIVEN
        final var companyId = 3L;
        final var company = new Company(companyId, "wbt", "work beats talent");
        when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));
        final var updateRequest = new CompanyRequest("wbt", "work always beats talent");
        final var argumentCaptor = ArgumentCaptor.forClass(Company.class);
        // WHEN
        final var actualResponse = underTest.update(companyId, updateRequest);
        verify(companyRepository).save(argumentCaptor.capture());
        // THEN
        final var captured = argumentCaptor.getValue();
        assertThat(actualResponse).isTrue();
        assertThat(captured.getId()).isEqualTo(companyId);
        assertThat(captured.getName()).isEqualTo(updateRequest.name());
        assertThat(captured.getDescription()).isEqualTo(updateRequest.description());
    }

    @Test
    void updateNonExistingCompanyThrowException() {
        // GIVEN
        final var someId = -1L;
        when(companyRepository.findById(someId)).thenReturn(Optional.empty());
        // WHEN
        assertThatThrownBy(() -> underTest.update(someId, any(CompanyRequest.class)))
                .isInstanceOf(CompanyNotFoundException.class)
                .hasMessage("Company resource with Id %s not found".formatted(someId));
        // THEN
        verify(companyRepository, never()).save(any(Company.class));
    }

    @Test
    void delete() {
        // GIVEN
        final var companyId = 5L;
        final var company = new Company(companyId, "wbt", "work beats talent");
        when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));
        // WHEN
        final var actualResponse = underTest.delete(companyId);
        // THEN
        verify(companyRepository).deleteById(companyId);
        assertThat(actualResponse).isTrue();
    }

    @Test
    void deleteNonExistingCompanyThrowException() {
        // GIVEN
        final var nonExistingCompanyId = -5L;
        when(companyRepository.findById(nonExistingCompanyId)).thenReturn(Optional.empty());
        // WHEN
        assertThatThrownBy(() -> underTest.delete(nonExistingCompanyId))
                .isInstanceOf(CompanyNotFoundException.class)
                .hasMessage("Company resource with Id %s not found".formatted(nonExistingCompanyId));
        // THEN
        verify(companyRepository, never()).deleteById(nonExistingCompanyId);
    }
}