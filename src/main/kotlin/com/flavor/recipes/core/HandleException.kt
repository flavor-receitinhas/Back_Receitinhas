package com.flavor.recipes.core

import io.sentry.Sentry
import org.springframework.http.ResponseEntity

class HandleException<T> {
    fun handle(e: Exception) : ResponseEntity<T>{
        if (e is BusinessException) {
            return ResponseEntity.unprocessableEntity().build()
        }
        Sentry.captureException(e)
        return ResponseEntity.internalServerError().build()
    }
}