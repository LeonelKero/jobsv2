package com.wbt.jobmicroservice.job

import com.wbt.jobmicroservice.job.external.CompanyResponse
import com.wbt.jobmicroservice.job.external.ReviewResponse
import java.time.LocalDateTime

data class JobResponse(
    val id: Long,
    val title: String,
    val description: String,
    val maxSalary: Double,
    val minSalary: Double,
    val createdAt: LocalDateTime,
    val location: String,
    val company: CompanyResponse?,
    val reviews: Array<ReviewResponse>?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as JobResponse

        if (id != other.id) return false
        if (title != other.title) return false
        if (description != other.description) return false
        if (maxSalary != other.maxSalary) return false
        if (minSalary != other.minSalary) return false
        if (createdAt != other.createdAt) return false
        if (location != other.location) return false
        if (company != other.company) return false
        if (reviews != null) {
            if (other.reviews == null) return false
            if (!reviews.contentEquals(other.reviews)) return false
        } else if (other.reviews != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + maxSalary.hashCode()
        result = 31 * result + minSalary.hashCode()
        result = 31 * result + createdAt.hashCode()
        result = 31 * result + location.hashCode()
        result = 31 * result + (company?.hashCode() ?: 0)
        result = 31 * result + (reviews?.contentHashCode() ?: 0)
        return result
    }
}
