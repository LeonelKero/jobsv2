package com.wbt.jobmicroservice.job.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
class JobConfig {
    @Bean
    fun getRestTemplate() = RestTemplate()
}