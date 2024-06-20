package com.together_english.deiz.data.member.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size


data class SignUpDto(
        @field:NotEmpty(message = "Name is required")
        val name: String,

        @field:Email(message = "Email should be valid")
        @field:NotEmpty(message = "Email is required")
        val email: String,

        @field:NotEmpty(message = "Password is required")
        @field:Size(min = 6, message = "Password should have at least 6 characters")
        val password: String,

        val phone: String? = null,
        val profile: String? = null
)