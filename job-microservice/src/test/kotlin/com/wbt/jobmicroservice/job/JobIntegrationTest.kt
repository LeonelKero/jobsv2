package com.wbt.jobmicroservice.job

import com.github.tomakehurst.wiremock.junit5.WireMockTest
import com.wbt.jobmicroservice.job.config.AbstractTestContainerTest
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@WireMockTest
class JobIntegrationTest : AbstractTestContainerTest() {

    private val jobURI = "/api/v1/jobs"

    @Autowired
    private lateinit var jobRepository: JobRepository

    @Autowired
    private lateinit var testRestTemplate: TestRestTemplate

    @Autowired
    private lateinit var jobService: JobService

    @BeforeEach
    fun setUp() {
        jobRepository.deleteAll()
    }

    @Test
    fun containerConnectionTest() {
        assertThat(postgreSQLContainer.isRunning).isTrue()
        assertThat(postgreSQLContainer.isCreated).isTrue()
    }

    @Test
    fun jobs() {
        // GIVEN -> a job request
        val companyId = 1L
        val jobRequest = JobRequest(
            "React developer",
            "Looking for junior react developer with solid foundation",
            400.0,
            1_200.0,
            "Ziginshor",
            companyId
        )
        // store job request to database
        val headers = HttpHeaders()
        headers.set("Content-Type", "application/json")
        testRestTemplate
            .exchange(
                jobURI,
                HttpMethod.POST,
                HttpEntity<JobRequest>(jobRequest, headers),
                String::class.java
            )
        // WHEN -> fetching all
        val response = testRestTemplate
            .exchange(
                jobURI,
                HttpMethod.GET,
                null,
                Array<JobResponse>::class.java
            )
        // THEN -> confirm these assertions
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body?.size).isEqualTo(1)
    }

    @Test
    fun job() {
        // GIVEN -> a job request
        val companyId = 1L
        val jobRequest = JobRequest(
            "React developer",
            "Looking for junior react developer with solid foundation",
            400.0,
            1_200.0,
            "Ziginshor",
            companyId
        )
        // store job request to database
        val headers = HttpHeaders()
        headers.set("Content-Type", "application/json")
        val message = testRestTemplate
            .exchange(
                jobURI,
                HttpMethod.POST,
                HttpEntity<JobRequest>(jobRequest, headers),
                String::class.java
            )
        // Mock external api cal
        // Todo: handle external service call
        // WHEN
        val response = testRestTemplate
            .exchange(
                "$jobURI/{id}",
                HttpMethod.GET,
                null,
                JobResponse::class.java,
                1
            )
        // THE
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    fun jobsLowSalaries() {
        // GIVEN

        // WHEN

        // THEN
    }

    @Test
    fun jobsHeightSalaries() {
        // GIVEN

        // WHEN

        // THEN
    }

    @Test
    fun add() {
        // GIVEN
        val companyId = 1L
        val jobRequest = JobRequest(
            "React developer",
            "Looking for junior react developer with solid foundation",
            400.0,
            1_200.0,
            "Ziginshor",
            companyId
        )
        // WHEN
        val response = testRestTemplate.exchange(
            jobURI,
            HttpMethod.POST,
            HttpEntity<JobRequest>(jobRequest),
            String::class.java
        )
        // THEN
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isEqualTo("Job successfully saved")
    }

    @Test
    fun deleteJob() {
        // GIVEN
        // forge nd post a job
        val jobId = 1L
        val companyId = 1L
        val jobRequest = JobRequest(
            "React developer",
            "Looking for junior react developer with solid foundation",
            400.0,
            1_200.0,
            "Ziginshor",
            companyId
        )
        testRestTemplate.exchange(
            jobURI,
            HttpMethod.POST,
            HttpEntity<JobRequest>(jobRequest),
            String::class.java
        )
        // WHEN -> perform delete
        val response = testRestTemplate
            .exchange(
                "$jobURI/{id}",
                HttpMethod.DELETE,
                null,
                Boolean::class.java,
                jobId
            )
        // THEN
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isTrue()
    }

    @Test
    fun whenTryingToDeleteNoneExistingJobThrowException() {
        // GIVEN
        val fakeJobId = -1L
        // WHEN
        assertThatThrownBy {
            testRestTemplate
                .exchange(
                    "$jobURI/{id}",
                    HttpMethod.DELETE,
                    null,
                    Boolean::class.java,
                    fakeJobId
                )
        }.hasMessage("Job resource with Id $fakeJobId not found")
    }

    @Test
    fun updateJob() {
        // GIVEN

        // WHEN

        // THEN
    }
}