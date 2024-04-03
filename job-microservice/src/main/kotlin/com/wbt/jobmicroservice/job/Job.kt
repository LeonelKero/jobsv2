package com.wbt.jobmicroservice.job

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Entity
class Job(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long? = null,
    val title: String,
    val description: String,
    val minSalary: Double,
    val maxSalary: Double,
    @CreationTimestamp val createdAt: LocalDateTime? = null,
    @UpdateTimestamp val updatedAt: LocalDateTime? = null,
    val location: String,
    val companyId: Long
)
