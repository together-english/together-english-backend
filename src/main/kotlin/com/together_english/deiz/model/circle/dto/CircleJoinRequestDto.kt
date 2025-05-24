package com.together_english.deiz.model.circle.dto


import com.together_english.deiz.model.circle.CircleJoinStatus
import io.swagger.v3.oas.annotations.media.Schema
import java.util.*

@Schema(description = "모임 참가요청 상세정보 응답 DTO")
data class CircleJoinRequestDto(
    @Schema(description = "모임 가입신청 ID", example = "afcf6ddb-0809-490f-88a6-5a23710c6d80")
    val circleJoinRequestId: UUID,

    @Schema(description = "멤버 ID", example = "65545867-b973-41f8-8633-a471435ae48d")
    val memberId: UUID,

    @Schema(description = "서클 ID", example = "12345678-1234-1234-1234-1234567890ab")
    val circleId: UUID,

    @Schema(description = "서클 이름", example = "영어 스터디")
    val circleName: String,

    @Schema(description = "가입신청 상태", example = "PENDING")
    val status: CircleJoinStatus,

    @Schema(description = "가입 요청 메시지", example = "이 모임에 참여하고 싶습니다!")
    val message: String?,

    @Schema(description = "닉네임", example = "닉네임")
    val nickname: String,

    @Schema(description = "유저 프로필", example = "http//profile.com")
    val profile: String?,

    @Schema(description = "생성 일시", example = "2025-05-18T10:00:00")
    val createdAt: String,

    @Schema(description = "수정 일시", example = "2025-05-18T10:00:00")
    val updatedAt: String
)