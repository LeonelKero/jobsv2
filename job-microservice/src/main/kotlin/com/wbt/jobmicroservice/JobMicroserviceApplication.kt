package com.wbt.jobmicroservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class JobMicroserviceApplication

fun main(args: Array<String>) {
	runApplication<JobMicroserviceApplication>(*args)
}
