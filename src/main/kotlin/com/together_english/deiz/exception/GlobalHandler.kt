package com.together_english.deiz.exception

import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.security.SignatureException

@ControllerAdvice
class GlobalHandler {

    @ExceptionHandler(JWTException::class)
    fun jwtException(ex: JWTException): ResponseEntity<Any> {

        return ResponseEntity.badRequest().body(ex.message)

    }


}