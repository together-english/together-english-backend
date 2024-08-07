package com.together_english.deiz.controller

import com.together_english.deiz.data.JwtToken
import com.together_english.deiz.data.MainResponse
import com.together_english.deiz.data.MainResponse.Companion.getSuccessResponse
import com.together_english.deiz.data.member.dto.SignUpRequest
import com.together_english.deiz.data.member.dto.SignInRequest
import com.together_english.deiz.service.AuthService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.aspectj.weaver.ast.Not
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
@Tag(name = "인증 API", description = "유저 인증을 위한 API")
class AuthController(
        private val authService: AuthService
) {

    @Operation(summary = "회원 가입", description = "회원가입 요청")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Operation completed successfully."),
        ApiResponse(responseCode = "400", description = "Bad Request: Invalid input data.")
    ])
    @PostMapping("/sign-up")
    fun signUp(@Valid @RequestBody signUpRequest: SignUpRequest): ResponseEntity<MainResponse<Nothing>> {
        authService.signUp(signUpRequest)
        return ResponseEntity.ok(getSuccessResponse())
    }

    @Operation(summary = "로그인", description = "로그인 요청")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Operation completed successfully."),
        ApiResponse(responseCode = "400", description = "Bad Request: Invalid input data.")
    ])
    @PostMapping("/sign-in")
    fun signIn(@Valid @RequestBody signInRequest: SignInRequest): ResponseEntity<MainResponse<JwtToken>> {
        val token = authService.signIn(signInRequest)
        return ResponseEntity.ok(getSuccessResponse(token))
    }
}