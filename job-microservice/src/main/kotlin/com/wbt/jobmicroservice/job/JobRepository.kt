package com.wbt.jobmicroservice.job

import org.springframework.data.jpa.repository.JpaRepository

interface JobRepository : JpaRepository<Job, Long> {
    fun findByMaxSalaryBetween(maxDown: Double, maxUp: Double): List<Job>
    fun findByMinSalaryBetween(minDown: Double, minUp: Double): List<Job>
}