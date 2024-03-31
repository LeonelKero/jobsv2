package com.wbt.companymicroservice.company;

import com.wbt.companymicroservice.exception.CompanyAlreadyExistException;
import com.wbt.companymicroservice.exception.CompanyNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public record CompanyService(CompanyRepository companyRepository) {


    public List<CompanyResponse> fetchAll() {
        return this.companyRepository
                .findAll()
                .stream()
                .map(company -> new CompanyResponse(company.getId(), company.getName(), company.getDescription()))
                .collect(Collectors.toList());
    }

    public Optional<CompanyResponse> findById(final Long companyId) {
        return this.companyRepository.findById(companyId)
                .map(company -> new CompanyResponse(company.getId(), company.getName(), company.getDescription()));
    }

    public List<CompanyResponse> fetchByName(final String description) {
        return this.companyRepository.findByDescription(description)
                .stream()
                .map(company -> new CompanyResponse(company.getId(), company.getName(), company.getDescription()))
                .collect(Collectors.toList());
    }

    public Boolean create(final CompanyRequest request) {
        final var name = request.name();
        if (this.companyRepository.existsByName(name))
            throw new CompanyAlreadyExistException("Company name %s already taken".formatted(name));
        this.companyRepository.save(new Company(request.name(), request.description()));
        return true;
    }

    public Boolean update(final Long companyId, final CompanyRequest request) {
        final var optionalCompany = this.companyRepository.findById(companyId);
        if (optionalCompany.isPresent()) {
            this.companyRepository.save(new Company(companyId, request.name(), request.description()));
            return true;
        }
        throw new CompanyNotFoundException("Company resource with Id %s not found".formatted(companyId));
    }

    public Boolean delete(final Long companyId) {
        final var optionalCompany = this.companyRepository.findById(companyId);
        if (optionalCompany.isEmpty())
            throw new CompanyNotFoundException("Company resource with Id %s not found".formatted(companyId));
        this.companyRepository.deleteById(companyId);
        return true;
    }
}
