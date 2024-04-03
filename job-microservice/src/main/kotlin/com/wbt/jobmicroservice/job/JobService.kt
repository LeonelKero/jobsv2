package com.wbt.jobmicroservice.job

import com.wbt.jobmicroservice.job.exception.JobNotFoundException
import com.wbt.jobmicroservice.job.external.Company
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject
import java.util.*

@Service
class JobService(val jobRepository: JobRepository) {

    private val companyServiceUrl: String = "http://localhost:8082/api/v1/companies"

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
        RestTemplate().getForObject(
            "$companyServiceUrl/{id}",
            Company::class,
            it.companyId
        )
    )

    fun findById(jobId: Long): Optional<JobResponse> {
        val result = jobRepository.findById(jobId)
        if (result.isEmpty) throw JobNotFoundException("Job resource with Id $jobId not found")
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

    fun jobsWithMaxSalaryRange(maxLow: Double, maxTop: Double): List<JobResponse> {
        val jobs = jobRepository.findByMaxSalaryBetween(maxLow, maxTop)
        return jobs.stream().map { toJobResponse(it) }.toList()
    }

    fun jobsWithLowSalaryRange(minLow: Double, minTop: Double): List<JobResponse> {
        return jobRepository
            .findByMinSalaryBetween(minLow, minTop)
            .stream()
            .map { toJobResponse(it) }
            .toList()
    }

    fun delete(jobId: Long): Boolean {
        val optionalJob = jobRepository.findById(jobId)
        if (optionalJob.isEmpty) throw JobNotFoundException("Job resource with Id $jobId not found")
        jobRepository.delete(optionalJob.get())
        return true
    }

    fun update(jobId: Long, jobRequest: JobRequest): Boolean {
        val optionalJob = jobRepository.findById(jobId)
        if (optionalJob.isEmpty) throw JobNotFoundException("Job resource with Id $jobId not found")
        var recentJob = Job(
            id = jobId,
            title = jobRequest.title,
            description = jobRequest.description,
            minSalary = jobRequest.minSalary,
            maxSalary = jobRequest.maxSalary,
            location = jobRequest.location,
            companyId = jobRequest.company
        )
        jobRepository.save(recentJob)
        return true
    }
}