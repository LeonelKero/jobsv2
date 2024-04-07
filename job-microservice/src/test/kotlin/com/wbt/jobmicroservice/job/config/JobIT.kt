package com.wbt.jobmicroservice.job.config

import com.wbt.jobmicroservice.job.JobResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class JobIT : AbstractTestContainerTest() {

    private val jobUri: String = "/api/v1/jobs"

    @Autowired
    private lateinit var testRestTemplate: TestRestTemplate

    companion object {
        @JvmStatic
        @BeforeAll
        fun setUp(): Unit {
            postgreSQLContainer.start()
        }

        @JvmStatic
        @AfterAll
        fun tearDown(): Unit {
            postgreSQLContainer.stop()
        }
    }

    @Test
    fun containerUpTest() {
        assertThat(postgreSQLContainer.isCreated).isTrue()
        assertThat(postgreSQLContainer.isRunning).isTrue()
    }

    @Test
    fun fetchJobsTest() {
        // GIVEN  // WHEN
        val response = testRestTemplate.exchange(
            jobUri,
            HttpMethod.GET,
            null,
            Array<JobResponse>::class.java
        )
        // THEN
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }
}