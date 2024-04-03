package com.wbt.jobmicroservice.job.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
class UnSupportedJobOperationException(message: String) : RuntimeException(message)