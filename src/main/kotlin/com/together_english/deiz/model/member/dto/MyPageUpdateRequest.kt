package com.together_english.deiz.model.member.dto

import com.together_english.deiz.model.member.Gender
import io.swagger.v3.oas.annotations.media.Schema


@Schema(description = "마이 페이지 업데이트 요청")
class MyPageUpdateRequest(
        @Schema(description = "이름", example = "홍영기")
        val name: String,
        @Schema(description = "닉네임", example = "이카루스")
        val nickname: String,
        @Schema(description = "기존 패스워드", example = "dsfsf23432")
        val currentPassword: String,
        @Schema(description = "새로운 패스워드", example = "ds23432")
        val newPassword: String,
        @Schema(description = "마케팅 수신 동의", example = "true")
        val isMarketingAgreed: Boolean,
        @Schema(description = "나이", example = "25")
        val age: Int? = null,
        @Schema(description = "성별", example = "F")
        val gender: Gender = Gender.NO
)