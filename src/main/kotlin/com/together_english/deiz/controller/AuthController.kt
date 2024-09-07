package com.together_english.deiz.controller

import com.together_english.deiz.model.common.MainResponse
import com.together_english.deiz.model.common.MainResponse.Companion.getSuccessResponse
import com.together_english.deiz.model.member.dto.SignUpRequest
import com.together_english.deiz.model.member.dto.SignInRequest
import com.together_english.deiz.model.member.dto.SignInResponse
import com.together_english.deiz.service.AuthService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
@Tag(name = "인증 API", description = "유저 인증을 위한 API")
class AuthController(
        private val authService: AuthService
) {

    @Operation(summary = "회원 가입", description = "회원가입 요청")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Operation completed successfully."),
        ApiResponse(responseCode = "400", description = "Bad Request: Invalid input data.")
    ])
    @PostMapping("/signup")
    fun signUp(@Valid @RequestBody signUpRequest: SignUpRequest): ResponseEntity<MainResponse<Nothing>> {
        authService.signUp(signUpRequest)
        return ResponseEntity.ok(getSuccessResponse())
    }

    @Operation(summary = "로그인", description = "로그인 요청")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Operation completed successfully."),
        ApiResponse(responseCode = "400", description = "Bad Request: Invalid input data.")
    ])
    @PostMapping("/login")
    fun signIn(@Valid @RequestBody signInRequest: SignInRequest): ResponseEntity<MainResponse<SignInResponse>> {
        val signInResponse = authService.signIn(signInRequest)
        return ResponseEntity.ok(getSuccessResponse(signInResponse))
    }
}