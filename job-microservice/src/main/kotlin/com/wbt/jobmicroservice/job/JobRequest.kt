package com.wbt.jobmicroservice.job

data class JobRequest(
    val title: String,
    val description: String,
    val minSalary: Double,
    val maxSalary: Double,
    val location: String
)
