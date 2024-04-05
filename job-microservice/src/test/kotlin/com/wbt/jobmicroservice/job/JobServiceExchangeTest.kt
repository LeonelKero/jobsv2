package com.wbt.jobmicroservice.job

import com.wbt.jobmicroservice.job.external.CompanyResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.web.client.RestTemplate
import java.time.LocalDateTime
import java.util.*

@ExtendWith(SpringExtension::class)
@SpringBootTest
class JobServiceExchangeTest {

    @Autowired
    private lateinit var underTest: JobService;

    @MockBean
    private lateinit var repository: JobRepository

    @MockBean
    private lateinit var restTemplate: RestTemplate

    @Test
    fun jobCompanyExchange() {
        val companyId = 1L
        Mockito
            .`when`(
                restTemplate.exchange(
                    "http://localhost:8082/api/v1/companies/{id}",
                    HttpMethod.GET,
                    null,
                    CompanyResponse::class.java,
                    companyId
                )
            )
            .thenReturn(
                ResponseEntity.ok(CompanyResponse(companyId, "wbt", "work beats talent"))
            )
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
        Mockito.`when`(repository.findById(jobId)).thenReturn(Optional.of(job))
        val actualResponse = underTest.findById(jobId)

        Mockito.verify(repository).findById(jobId)
        assertThat(actualResponse.company).isNotNull
    }
}