package com.wbt.jobmicroservice.job.external

import java.time.LocalDateTime

data class ReviewResponse(
    val id: Long,
    val title: String,
    val rating: Double,
    val content: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)
