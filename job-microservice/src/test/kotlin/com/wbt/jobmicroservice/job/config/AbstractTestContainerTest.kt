package com.wbt.jobmicroservice.job.config

import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
abstract class AbstractTestContainerTest {

    companion object{
        @Container
        @ServiceConnection
        val postgreSQLContainer = PostgreSQLContainer("postgres:16")
    }

 }