package com.together_english.deiz.model.member.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "회원가입 요청")
data class SignUpRequest(

        @field:NotEmpty(message = "Name is required")
        @Schema(description = "이름", example = "파카")
        val name: String,

        @Schema(description = "닉네임", example = "제트")
        val nickname: String?,

        @field:Email(message = "Email should be valid")
        @field:NotEmpty(message = "Email is required")
        @Schema(description = "이메일", example = "paka@example.com")
        val email: String,

        @field:NotEmpty(message = "Password is required")
        @field:Size(min = 6, message = "Password should have at least 6 characters")
        @Schema(description = "패스워드", example = "securePassword123")
        val password: String,

        @Schema(description = "휴대폰 (optional)", example = "123-456-7890")
        val phone: String? = null,

        @Schema(description = "프로필 URL (optional)", example = "Profile description or URL")
        val profile: String? = null,

        @Schema(description = "필수 이용약관 동의 여부", example = "true")
        val isTermsAgreed: Boolean,

        @Schema(description = "개인정보 처리방침 동의 여부", example = "true")
        val isPrivacyAgreed: Boolean,

        @Schema(description = "마케팅 정보 수신 동의 여부", example = "true")
        val isMarketingAgreed: Boolean = false

)