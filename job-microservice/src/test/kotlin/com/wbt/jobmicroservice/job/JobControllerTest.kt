package com.wbt.jobmicroservice.job

import com.wbt.jobmicroservice.job.config.AbstractTestContainerTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class JobControllerTest : AbstractTestContainerTest() {

    private val jobURI = "/api/v1/jobs"

    @Autowired
    private lateinit var jobRepository: JobRepository

    @Autowired
    private lateinit var testRestTemplate: TestRestTemplate

    @BeforeEach
    fun setUp() {
        //jobRepository.deleteAll()
    }

    @Test
    fun containerConnectionTest() {
        assertThat(postgreSQLContainer.isRunning).isTrue()
        assertThat(postgreSQLContainer.isCreated).isTrue()
    }

    @Test
    fun jobs() {
        // GIVEN
//        val companyId = 1L
//        val jobRequest = JobRequest(
//            "React developer",
//            "Looking for junior react developer with solid foundation",
//            400.0,
//            1_200.0,
//            "Ziginshor",
//            companyId
//        )
//        // store one record to database
//        testRestTemplate
//            .exchange(
//                jobURI,
//                HttpMethod.POST,
//                HttpEntity(jobRequest),
//                ResponseEntity::class.java
//            )
        // WHEN
        val response = testRestTemplate.exchange(
            jobURI,
            HttpMethod.GET,
            null,
            ResponseEntity::class.java
        )
        // THEN
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    fun job() {
        // GIVEN

        // WHEN

        // THEN
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

        // WHEN

        // THEN
    }

    @Test
    fun deleteJob() {
        // GIVEN

        // WHEN

        // THEN
    }

    @Test
    fun updateJob() {
        // GIVEN

        // WHEN

        // THEN
    }
}