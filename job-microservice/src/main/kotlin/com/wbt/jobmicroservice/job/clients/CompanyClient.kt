package com.wbt.jobmicroservice.job.clients

import com.wbt.jobmicroservice.job.external.CompanyResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@FeignClient(name = "COMPANY-MICROSERVICE")
interface CompanyClient {
    @GetMapping(path = ["/api/v1/companies/{id}"])
    fun getCompany(@PathVariable(name = "id") id: Long): CompanyResponse
}