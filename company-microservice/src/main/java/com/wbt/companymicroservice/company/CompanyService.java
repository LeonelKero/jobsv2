package com.wbt.companymicroservice.company;

import org.springframework.stereotype.Service;

@Service
public record CompanyService(CompanyRepository companyRepository) {

}
