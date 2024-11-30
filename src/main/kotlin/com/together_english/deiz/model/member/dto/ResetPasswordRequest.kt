package com.together_english.deiz.model.member.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

@Schema(description = "비밀번호 재설정 요청")
data class ResetPasswordRequest(
    @Schema(description = "발급한 권한 키", example = "dsada-231dsa-dsadsff")
    val authKey: String,

    @Schema(description = "새로운 패스워드", example = "ds23432")
    @field:NotBlank(message = "비밀번호는 필수 입력 사항입니다.")
    val newPassword: String,
) {

}