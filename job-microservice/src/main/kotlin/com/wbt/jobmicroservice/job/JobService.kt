package com.wbt.jobmicroservice.job

import com.wbt.jobmicroservice.job.external.Company
import org.springframework.stereotype.Service
import java.util.*

@Service
class JobService(val jobRepository: JobRepository) {

    fun findAll(): List<JobResponse> {
        return jobRepository
            .findAll()
            .stream()
            .map {
                toJobResponse(it)
            }.toList()
    }

    private fun toJobResponse(it: Job) = JobResponse(
        it.id!!,
        it.title,
        it.description,
        it.maxSalary,
        it.minSalary,
        it.createdAt!!,
        it.location,
        Company(1L, "", "")
    )

    fun findById(jobId: Long): Optional<JobResponse> {
        val result = jobRepository.findById(jobId)
        if (result.isEmpty) throw JobNotFoundException("Job resource with Id {jobId} not found")
        return result.map { toJobResponse(it) }
    }

    fun save(jobRequest: JobRequest, company: Long): Boolean {
        val result = jobRepository.save(
            Job(
                title = jobRequest.title,
                description = jobRequest.description,
                minSalary = jobRequest.minSalary,
                maxSalary = jobRequest.maxSalary,
                location = jobRequest.location,
                companyId = company
            )
        )
        return result.id != null && result.createdAt != null && result.updatedAt != null
    }

    // Todo: delete, update
}