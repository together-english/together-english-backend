package com.together_english.deiz.model.circle.dto

import com.together_english.deiz.model.circle.CircleJoinStatus
import com.together_english.deiz.model.member.Gender
import io.swagger.v3.oas.annotations.media.Schema
import java.util.*

@Schema(description = "모임 참가요청 상세정보 응답 DTO")
data class CircleJoinDetailResponse(
    @Schema(description = "모임 가입신청 ID", example = "afcf6ddb-0809-490f-88a6-5a23710c6d80")
    val circleJoinRequestId: UUID,
    @Schema(description = "닉네임", example = "탠겐")
    val nickname: String,
    @Schema(description = "프로필 URL", example = "profile.link.com")
    val profile: String?,
    @Schema(description = "성별", example = "F")
    val gender: Gender,
    @Schema(description = "나이", example = "23")
    val age: Int,
    @Schema(description = "가입신청 메시지", example = "해당 모임에 가입신청합니다.")
    val message: String,
    @Schema(description = "가입신청 상태", example = "PENDING")
    val status: CircleJoinStatus
) {

}