package com.together_english.deiz.data.member.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty

data class SignInDto(
        @field:Email(message = "Email should be valid")
        @field:NotEmpty(message = "Email is required")
        @Schema(description = "이메일", example = "paka@example.com")
        val email: String,

        @field:NotEmpty(message = "Password is required")
        @Schema(description = "패스워드", example = "password123")
        val password: String
)