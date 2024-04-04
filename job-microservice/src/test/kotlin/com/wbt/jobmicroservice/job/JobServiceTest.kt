package com.wbt.jobmicroservice.job

import com.wbt.jobmicroservice.job.config.AbstractTestContainerTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.web.client.RestTemplate
import java.time.LocalDateTime
import java.util.*

@ExtendWith(MockitoExtension::class)
class JobServiceTest : AbstractTestContainerTest() {

    @InjectMocks
    private lateinit var underTest: JobService

    @Mock
    private lateinit var jobRepository: JobRepository

    @Mock
    private lateinit var restTemplate: RestTemplate

    @BeforeEach
    fun setUp() {
        underTest = JobService(jobRepository)
    }

    @Test
    fun isContainerSetup() {
        assertThat(postgreSQLContainer.isCreated).isTrue()
        assertThat(postgreSQLContainer.isRunning).isTrue()
    }

    @Test
    fun findAll() {
        // GIVEN // WHEN
        underTest.findAll()
        // THEN
        verify(jobRepository).findAll()
    }

    data class CompanyRequest(private val name: String, private val description: String)

    @Test
    fun findById() {
        /*
            FOR THIS TEST TO WORK
            EXTERNAL SERVICE SHOULD BE UP AND RUNNING - AND WITH ACCESS TO DATABASE
         */
        // GIVEN
        val id = 1L
        val companyId = 1L
        val job = Job(
            id,
            "Java developer",
            "Intermediate java developer",
            1400.0,
            3_000.0,
            LocalDateTime.now(),
            LocalDateTime.now(),
            "Douala",
            companyId
        )
        `when`(jobRepository.findById(id)).thenReturn(Optional.of(job))
        // WHEN
        val actualResult = underTest.findById(id)
        // THEN
        verify(jobRepository).findById(id)
        assertThat(actualResult).isNotNull
    }

    @Test
    fun save() {
        // GIVEN

        // WHEN

        // THEN
    }

    @Test
    fun jobsWithMaxSalaryRange() {
        // GIVEN

        // WHEN

        // THEN
    }

    @Test
    fun jobsWithMinSalaryRange() {
        // GIVEN

        // WHEN

        // THEN
    }

    @Test
    fun delete() {
        // GIVEN

        // WHEN

        // THEN
    }

    @Test
    fun update() {
        // GIVEN

        // WHEN

        // THEN
    }
}