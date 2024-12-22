package com.together_english.deiz.model.circle.dto

import com.together_english.deiz.model.circle.CircleMember
import com.together_english.deiz.model.member.Gender
import io.swagger.v3.oas.annotations.media.Schema
import java.util.*

@Schema(description = "Response DTO for Circle Member details")
data class CircleMemberDetailResponse(
    @Schema(description = "모임 ID", example = "f4737cce-c4f9-426c-9878-ebe8461ad7e4")
    val circleId: UUID,
    @Schema(description = "멤버 닉네임", example = "영어러버")
    val nickname: String,
    @Schema(description = "멤버 이메일", example = "kodh10@gmail.com")
    val email: String,
    @Schema(description = "멤버 프로필", example = "profile.com")
    val profile: String,
    @Schema(description = "멤버 성별", example = "F, M, NO")
    val gender : Gender,
    @Schema(description = "멤버 나이", example = "29")
    val age: Int,
    @Schema(description = "멤버 역할", example = "LEADER, MEMBER")
    val role: CircleMember.CircleRole,
) {
}