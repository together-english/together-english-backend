package com.together_english.deiz.model.circle.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.util.*

@Schema(description = "모임 참가 요청 DTO")
data class CircleJoinUpdateRequestDTO(
    @field:NotNull(message = "모임 가입신청 ID는 필수 입력 사항입니다.")
    @Schema(description = "모임 가입신청 ID", example = "afcf6ddb-0809-490f-88a6-5a23710c6d80")
    val circleJoinRequestId: UUID,

    @field:Size(max = 3000, message = "Message cannot exceed 3000 characters")
    @Schema(description = "가입신청 메시지", example = "모임에 관심있어 신청드립니다.")
    val message: String,
)
