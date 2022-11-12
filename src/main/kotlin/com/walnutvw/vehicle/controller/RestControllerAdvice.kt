package com.walnutvw.vehicle.controller

import com.walnutvw.vehicle.config.Logging
import com.walnutvw.vehicle.exception.BadActionException
import com.walnutvw.vehicle.exception.NotFoundException
import com.walnutvw.vehicle.utils.logger
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.lang.RuntimeException

@RestControllerAdvice
class RestControllerAdvice: Logging {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException::class)
    fun handleNotFoundException(e: RuntimeException){
        logger().error("${e.javaClass.name}: ${e.message ?: "No message"}")
    }


    @ExceptionHandler(BadActionException::class)
    fun youShouldDoThat(e: Throwable): ResponseEntity<String> {
        logger().error("${e.javaClass.name} : ${e.message ?: "No Message"}")
        return ResponseEntity.badRequest().body(e.message)
    }
}