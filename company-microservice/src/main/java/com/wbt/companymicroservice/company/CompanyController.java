package com.wbt.companymicroservice.company;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = {"/api/v1/companies"})
public record CompanyController(CompanyService companyService) {
}
