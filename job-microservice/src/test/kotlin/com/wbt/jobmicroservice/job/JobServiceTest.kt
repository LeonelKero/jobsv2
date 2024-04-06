package com.wbt.jobmicroservice.job

import com.wbt.jobmicroservice.job.config.AbstractTestContainerTest
import com.wbt.jobmicroservice.job.external.CompanyResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.web.client.RestTemplate
import java.time.LocalDateTime
import java.util.*

@ExtendWith(MockitoExtension::class)
class JobServiceTest : AbstractTestContainerTest() {

    @Mock
    private lateinit var restTemplate: RestTemplate

    @InjectMocks
    private lateinit var underTest: JobService

    @Mock
    private lateinit var jobRepository: JobRepository

    @BeforeEach
    fun setUp() {
        underTest = JobService(jobRepository, restTemplate)
    }

    @Test
    fun isContainerSetup() {
        assertThat(postgreSQLContainer.isCreated).isTrue()
        assertThat(postgreSQLContainer.isRunning).isTrue()
    }

    @Test
    fun findAll() {
        // GIVEN
        Mockito.`when`(jobRepository.findAll()).thenReturn(
            listOf(
                Job(
                    1L,
                    "Java developer",
                    "Intermediate java developer",
                    1400.0,
                    3_000.0,
                    LocalDateTime.now(),
                    LocalDateTime.now(),
                    "Douala",
                    1L
                )
            )
        )
        Mockito.`when`(
            restTemplate.getForObject(
                "http://localhost:8082/api/v1/companies/{id}",
                CompanyResponse::class.java,
                1L
            )
        ).thenReturn(
            CompanyResponse(1L, "wbt", "work beats talent")
        )
        // WHEN
        underTest.findAll()
        // THEN
        Mockito.verify(jobRepository).findAll()
    }

    @Test
    fun whenFindJoBbyIdAlsoGetRelatedExistingCompanyDetail() {
        // GIVEN
        val companyId = 1L
        val jobId = 1L
        val job = Job(
            jobId,
            "Java developer",
            "Intermediate java developer",
            1400.0,
            3_000.0,
            LocalDateTime.now(),
            LocalDateTime.now(),
            "Douala",
            companyId
        )

        Mockito.`when`(jobRepository.findById(jobId)).thenReturn(Optional.of(job))

        Mockito.`when`(
            restTemplate.getForObject(
                "http://localhost:8082/api/v1/companies/{id}",
                CompanyResponse::class.java,
                companyId
            )
        ).thenReturn(CompanyResponse(companyId, "wbt", "work beats talent"))
        // WHEN
        val response = underTest.findById(jobId)
        // THEN
        Mockito.verify(jobRepository).findById(jobId)
        assertThat(response.company).isNotNull
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