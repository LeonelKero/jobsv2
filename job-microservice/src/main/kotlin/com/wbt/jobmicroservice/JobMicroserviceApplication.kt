package com.wbt.jobmicroservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.openfeign.EnableFeignClients

@EnableFeignClients
@SpringBootApplication
@EnableDiscoveryClient
class JobMicroserviceApplication

fun main(args: Array<String>) {
    runApplication<JobMicroserviceApplication>(*args)
}
