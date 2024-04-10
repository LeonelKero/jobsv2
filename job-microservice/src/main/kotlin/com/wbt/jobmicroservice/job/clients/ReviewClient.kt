package com.wbt.jobmicroservice.job.clients

import com.wbt.jobmicroservice.job.external.ReviewResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(name = "REVIEW-MICROSERVICE")
interface ReviewClient {
    @GetMapping(path = ["/api/v1/reviews?company={id}"])
    fun getCompanyReviews(@RequestParam(name = "company") companyId: Long): Array<ReviewResponse>
}