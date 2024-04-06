package com.wbt.jobmicroservice.job

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(path = ["/api/v1/jobs"])
class JobController(private val jobService: JobService) {
    @GetMapping
    fun jobs() = ResponseEntity.ok(jobService.findAll())

    @GetMapping(path = ["/{id}"])
    fun job(@PathVariable id: Long) = ResponseEntity.ok(jobService.findById(id))

    @GetMapping(path = ["/min/salary"])
    fun jobsLowSalaries(@PathVariable low: Double, @PathVariable top: Double) =
        ResponseEntity(jobService.jobsWithMinSalaryRange(low, top), HttpStatus.OK)

    @GetMapping(path = ["/max/salary"])
    fun jobsHeightSalaries(@PathVariable low: Double, @PathVariable top: Double) =
        ResponseEntity(jobService.jobsWithMaxSalaryRange(low, top), HttpStatus.OK)

    @PostMapping
    fun add(@RequestBody job: JobRequest): ResponseEntity<String> {
        val isSaved = jobService.save(job, job.company)
        if (isSaved) return ResponseEntity.ok("Job successfully saved")
        return ResponseEntity(HttpStatus.BAD_REQUEST)
    }

    @DeleteMapping(path = ["/{id}"])
    fun deleteJob(@PathVariable id: Long) = ResponseEntity(jobService.delete(id), HttpStatus.OK)

    @PutMapping(path = ["/{id}"])
    fun updateJob(@PathVariable id: Long, @RequestBody request: JobRequest): ResponseEntity<String> {
        val isUpdated = jobService.update(id, request)
        if (isUpdated) return ResponseEntity("Job resource updated", HttpStatus.OK)
        return ResponseEntity(HttpStatus.BAD_REQUEST)
    }

}