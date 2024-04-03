package com.wbt.jobmicroservice.job

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(code = HttpStatus.NOT_FOUND)
class JobNotFoundException(message: String) : RuntimeException(message)