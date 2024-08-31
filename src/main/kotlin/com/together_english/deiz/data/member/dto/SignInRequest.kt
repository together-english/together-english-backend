package com.together_english.deiz.data.member.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty

@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "로그인 요청")
data class SignInRequest(
        @field:Email(message = "Email should be valid")
        @field:NotEmpty(message = "Email is required")
        @Schema(description = "이메일", example = "paka@example.com")
        val email: String,

        @field:NotEmpty(message = "Password is required")
        @Schema(description = "패스워드", example = "password123")
        val password: String
)