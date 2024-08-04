package com.together_english.deiz.exception

import com.together_english.deiz.common.ApiConstants
import com.together_english.deiz.data.MainResponse
import io.swagger.v3.oas.models.responses.ApiResponse
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import java.security.SignatureException

@ControllerAdvice
class GlobalHandler {
    @ExceptionHandler(Exception::class)
    @ResponseBody
    fun handleException(ex: Exception): ResponseEntity<Any> {
        return ResponseEntity.badRequest().body(MainResponse<Nothing>(
                status = ApiConstants.Status.ERROR,
                message = ex.message ?: ApiConstants.Message.BAD_REQUEST
        ))
    }
}