package com.flavor.recipes.core

import io.sentry.Sentry
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class RestResponseEntityExceptionHandler
    : ResponseEntityExceptionHandler() {

    @ExceptionHandler(BusinessException::class)
    fun handleBusinessError(e: BusinessException): ResponseEntity<Any> {
        return ResponseEntity<Any>(
            mapOf("message" to e.message),
            HttpStatus.UNPROCESSABLE_ENTITY
        )
    }

    @ExceptionHandler(Exception::class)
    fun handleInternalServerError(e: Exception): ResponseEntity<Any> {
        Sentry.captureException(e)
        LoggerFactory.getLogger("ExceptionHandler").trace(e.message, e)
        return ResponseEntity<Any>(
            mapOf("message" to "Ocorreu um erro no serviço"),
            HttpStatus.INTERNAL_SERVER_ERROR
        )
    }
}