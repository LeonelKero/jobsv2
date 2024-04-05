package com.wbt.jobmicroservice.job

import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.github.tomakehurst.wiremock.junit5.WireMockExtension
import com.github.tomakehurst.wiremock.junit5.WireMockTest
import com.wbt.jobmicroservice.job.config.AbstractTestContainerTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.RegisterExtension
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import java.time.LocalDateTime
import java.util.*

@ExtendWith(MockitoExtension::class)
@WireMockTest
class JobServiceTest : AbstractTestContainerTest() {

    @InjectMocks
    private lateinit var underTest: JobService

    @Mock
    private lateinit var jobRepository: JobRepository

    // CONFIGURE WIREMOCK
    companion object {
        @JvmStatic
        @RegisterExtension
        private val wireMockServer = WireMockExtension
            .newInstance()
            .options(WireMockConfiguration.wireMockConfig().dynamicPort())
            .build()
    }

    @DynamicPropertySource
    fun configureProperties(registry: DynamicPropertyRegistry): Unit {
        registry.add("http://localhost:8082/api/v1/companies", wireMockServer::baseUrl)
    }
    // END CONFIGURE WIREMOCK

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
            TODO: NEVER DO THAT
            FOR THIS TEST TO WORK
            EXTERNAL SERVICE SHOULD BE UP AND RUNNING - AND WITH ACCESS TO  ITS DATABASE
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
    fun whenFetchJobByIdGetCorrespondingCompany() { // Happy path (external service is up and running)
        // Given
        // first mock external api service call
        wireMockServer.stubFor(
            WireMock.get(WireMock.urlMatching("/.*")).willReturn(
                WireMock
                    .aResponse()
                    .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                    .withStatus(HttpStatus.OK.value())
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
        // Prepare request object and mock service call
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
        assertThat(actualResult.company).isNotNull
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