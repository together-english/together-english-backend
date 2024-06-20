package com.together_english.deiz.controller

import com.together_english.deiz.data.member.dto.SignUpDto
import com.together_english.deiz.data.member.dto.SignInDto
import com.together_english.deiz.service.AuthService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
        private val authService: AuthService
) {

    @PostMapping("/signup")
    fun signUp(@Valid @RequestBody signUpDto: SignUpDto): ResponseEntity<String> {
        authService.signUp(signUpDto)
        return ResponseEntity.ok("User registered successfully")
    }

    @PostMapping("/signin")
    fun signIn(@Valid @RequestBody signInDto: SignInDto): ResponseEntity<String> {
        val token = authService.signIn(signInDto)
        return ResponseEntity.ok(token)
    }
}