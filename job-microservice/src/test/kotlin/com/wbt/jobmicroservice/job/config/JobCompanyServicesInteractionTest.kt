package com.wbt.jobmicroservice.job.config

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.maciejwalkowiak.wiremock.spring.ConfigureWireMock
import com.maciejwalkowiak.wiremock.spring.EnableWireMock
import com.maciejwalkowiak.wiremock.spring.InjectWireMock
import com.wbt.jobmicroservice.job.Job
import com.wbt.jobmicroservice.job.JobRepository
import com.wbt.jobmicroservice.job.JobService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import java.time.LocalDateTime
import java.util.*


@ExtendWith(MockitoExtension::class)
@EnableWireMock(value = [ConfigureWireMock(name = "company-service", property = "company.api.service.url")])
class JobCompanyServicesInteractionTest : AbstractTestContainerTest() {

    @InjectWireMock(value = "company-service")
    private lateinit var wireMock: WireMockServer

    @Value("\${company.api.service.url}")
    private lateinit var wireMockUrl: String

    @InjectMocks
    private lateinit var underTest: JobService

    @Mock
    private lateinit var jobRepository: JobRepository

    @BeforeEach
    fun setUp() {
        underTest = JobService(jobRepository)
    }

    @Test
    fun whenGettingAJobWillCallExternalCompanyAPiTest() {

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
        // Mocking repository layer call
        `when`(jobRepository.findById(id)).thenReturn(Optional.of(job))
        // WHEN
        // Mock external company API call by Id at http://localhost:8082/api/v1/companies/{id}
        wireMock.stubFor(
            WireMock
                .get(WireMock.urlMatching("/.*"))
                .willReturn(
                    WireMock
                        .aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(
                            """
                            {
                          "id": 1,
                          "name": "PlayStation",
                          "description": "The game is just a start"
                        }
                        """.trimIndent()
                        )
                )
        )
        val response = underTest.findById(id)

        // THEN
        verify(jobRepository).findById(id)
        assertThat(response.company?.name).isEqualTo("PlayStation")
    }
}