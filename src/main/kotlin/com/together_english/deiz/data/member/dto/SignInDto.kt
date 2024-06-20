package com.together_english.deiz.data.member.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty

data class SignInDto(
        @field:Email(message = "Email should be valid")
        @field:NotEmpty(message = "Email is required")
        val email: String,

        @field:NotEmpty(message = "Password is required")
        val password: String
)