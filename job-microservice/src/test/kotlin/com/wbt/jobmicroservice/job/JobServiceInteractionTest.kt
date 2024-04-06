package com.wbt.jobmicroservice.job

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
class JobServiceInteractionTest {

    @Mock
    private lateinit var restTemplate: RestTemplate

    @Mock
    private lateinit var jobRepository: JobRepository

    @InjectMocks
    private lateinit var underTest: JobService

    @BeforeEach
    fun setUp() {
        underTest = JobService(jobRepository, restTemplate)
    }

    @Test
    fun whenFindJoBbyIdAlsoGetRelatedExistingCompanyDetail() {
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
        val response = underTest.findById(jobId)
        Mockito.verify(jobRepository).findById(jobId)
        assertThat(response.company).isNotNull
    }
}