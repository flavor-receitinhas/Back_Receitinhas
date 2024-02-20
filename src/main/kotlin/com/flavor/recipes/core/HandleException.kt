package com.flavor.recipes.core

import io.sentry.Sentry
import org.springframework.http.ResponseEntity

class HandleException<T> {
    fun handle(e: Exception) : ResponseEntity<Any>{
        if (e is BusinessException) {
            return ResponseEntity.unprocessableEntity().body(mapOf("message" to e.message))
        }
        Sentry.captureException(e)
        return ResponseEntity.internalServerError().build()
    }
}