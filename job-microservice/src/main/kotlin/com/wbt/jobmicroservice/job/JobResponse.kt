package com.wbt.jobmicroservice.job

import com.wbt.jobmicroservice.job.external.CompanyResponse
import java.time.LocalDateTime

data class JobResponse(
    val id: Long,
    val title: String,
    val description: String,
    val maxSalary: Double,
    val minSalary: Double,
    val createdAt: LocalDateTime,
    val location: String,
    val companyResponse: CompanyResponse
)
