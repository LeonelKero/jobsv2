package com.wbt.jobmicroservice.job

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.junit5.WireMockTest
import com.wbt.jobmicroservice.job.external.CompanyResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@WireMockTest(httpPort = 8082)
class JobWireMockIT {
    @Autowired
    private lateinit var jobService: JobService

    @Test
    fun getJobByIdTest() {
        val objectMapper = ObjectMapper()
        WireMock.stubFor(
            WireMock
                .get("http://localhost:8082/api/v1/companies/1")
                .willReturn(
                    WireMock
                        .ok()
                        .withHeader("Content-Type", "application/json")
                        .withJsonBody(objectMapper.valueToTree(CompanyResponse(1L, "wbt", "work beats talent")))
                )
        )
        // WHEN
        val result = jobService.findById(1L)
        // THEN
        assertThat(result).isNotNull
    }
}