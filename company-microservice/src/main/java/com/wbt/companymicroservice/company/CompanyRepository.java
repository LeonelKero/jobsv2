package com.wbt.companymicroservice.company;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    List<Company> findByDescription(final String description);

    Boolean existsByName(final String name);

}
