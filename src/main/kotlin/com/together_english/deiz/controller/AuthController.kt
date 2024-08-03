package com.together_english.deiz.controller

import com.together_english.deiz.data.member.dto.SignUpDto
import com.together_english.deiz.data.member.dto.SignInDto
import com.together_english.deiz.service.AuthService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
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
        ApiResponse(responseCode = "200", description = "User registered successfully"),
        ApiResponse(responseCode = "400", description = "Invalid input")
    ])
    @PostMapping("/sign-up")
    fun signUp(@Valid @RequestBody signUpDto: SignUpDto): ResponseEntity<String> {
        authService.signUp(signUpDto)
        return ResponseEntity.ok("User registered successfully")
    }

    @Operation(summary = "로그인", description = "로그인 요청")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "token value"),
        ApiResponse(responseCode = "400", description = "Invalid input")
    ])
    @PostMapping("/sign-in")
    fun signIn(@Valid @RequestBody signInDto: SignInDto): ResponseEntity<String> {
        val token = authService.signIn(signInDto)
        return ResponseEntity.ok(token)
    }
}