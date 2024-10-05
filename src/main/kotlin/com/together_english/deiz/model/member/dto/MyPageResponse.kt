package com.together_english.deiz.model.member.dto

import com.together_english.deiz.model.member.Gender
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "마이 페이지 조회 응답")
class MyPageResponse (
        @Schema(description = "멤버 ID", example = "FWEHFEWD32532332")
        val id: String,
        @Schema(description = "이름", example = "김철수")
        val name: String,
        @Schema(description = "이메일", example = "test@test.com")
        val email: String,
        @Schema(description = "닉네임", example = "탠겐")
        val nickname: String,
        @Schema(description = "프로필 URL", example = "profile.link.com")
        val profile: String?,
        @Schema(description = "성별", example = "F")
        val gender: Gender,
        @Schema(description = "나이", example = "23")
        val age: Int,
        @Schema(description = "마케팅 수신 동의", example = "true")
        val isMarketingAgreed: Boolean
)