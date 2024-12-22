package com.together_english.deiz.model.circle.dto

import com.together_english.deiz.model.circle.CircleMember
import io.swagger.v3.oas.annotations.media.Schema
import java.util.*

@Schema(description = "Response DTO for Circle Member details")
data class CircleMemberPageResponse(
    @Schema(description = "모임멤버 ID", example = "f4737cce-c4f9-426c-9878-ebe8461ad7e4")
    val circleMemberId: UUID,
    @Schema(description = "멤버 닉네임", example = "영어러버")
    val nickname: String,
    @Schema(description = "멤버 프로필", example = "profile.com")
    val profile: String?,
    @Schema(description = "멤버 역할", example = "LEADER, MEMBER")
    val role: CircleMember.CircleRole,
) {
}