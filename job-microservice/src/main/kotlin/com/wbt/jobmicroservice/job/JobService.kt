package com.wbt.jobmicroservice.job

import com.wbt.jobmicroservice.job.exception.JobNotFoundException
import com.wbt.jobmicroservice.job.exception.UnSupportedJobOperationException
import com.wbt.jobmicroservice.job.external.CompanyResponse
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class JobService(private val jobRepository: JobRepository, private val restTemplate: RestTemplate) {

    private val companyServiceUrl: String = "http://COMPANY-MICROSERVICE:8082/api/v1/companies"

    fun findAll(): List<JobResponse> {
        return jobRepository
            .findAll()
            .stream()
            .map {
                toJobResponse(it, restTemplate)
            }.toList()
    }

    fun findById(jobId: Long): JobResponse {
        val result = jobRepository.findById(jobId)
        if (result.isEmpty) throw JobNotFoundException("Job resource with Id $jobId not found")
        return result.map { toJobResponse(it, restTemplate) }.get()
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
        return jobs.stream().map { toJobResponse(it, restTemplate) }.toList()
    }

    fun jobsWithMinSalaryRange(minLow: Double, minTop: Double): List<JobResponse> {
        return jobRepository
            .findByMinSalaryBetween(minLow, minTop)
            .stream()
            .map { toJobResponse(it, restTemplate) }
            .toList()
    }

    fun delete(jobId: Long): Boolean {
        val optionalJob = jobRepository.findById(jobId)
        if (optionalJob.isEmpty) throw UnSupportedJobOperationException("Job resource with Id $jobId not found")
        jobRepository.delete(optionalJob.get())
        return true
    }

    fun update(jobId: Long, jobRequest: JobRequest): Boolean {
        val optionalJob = jobRepository.findById(jobId)
        if (optionalJob.isEmpty) throw UnSupportedJobOperationException("Job resource with Id $jobId not found")
        val recentJob = Job(
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

    private fun toJobResponse(it: Job, restTemplate: RestTemplate) = JobResponse(
        it.id!!,
        it.title,
        it.description,
        it.maxSalary,
        it.minSalary,
        it.createdAt!!,
        it.location,
        restTemplate.getForObject("$companyServiceUrl/{id}", CompanyResponse::class.java, it.companyId)
    )
}