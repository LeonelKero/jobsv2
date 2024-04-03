package com.wbt.jobmicroservice.job

import org.springframework.data.jpa.repository.JpaRepository

interface JobRepository : JpaRepository<Job, Long> {
    fun findByMaxSalaryBetween(maxDown: Double, maxUp: Double)
    fun findByMinSalaryBetween(minDown: Double, minUp: Double)
}