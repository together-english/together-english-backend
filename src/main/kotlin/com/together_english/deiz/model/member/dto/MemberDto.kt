package com.together_english.deiz.model.member.dto

import com.together_english.deiz.model.member.entity.Member
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "맴버 DTO")
class MemberDto(
        @Schema(description = "ID", example = "323432")
        val id: String,
        @Schema(description = "이름", example = "김철수")
        val name: String,
        @Schema(description = "이메일", example = "test@test.com")
        val email: String,
        @Schema(description = "닉네임", example = "탠겐")
        val nickname: String,
        @Schema(description = "프로필 URL", example = "profile.link.com")
        val profile: String?
) {
        companion object {
                fun memberToDto(member: Member): MemberDto {
                        return MemberDto(
                                id = member.id.toString(),
                                name = member.name,
                                email = member.email,
                                nickname = member.nickname,
                                profile = member.profile
                        )
                }
        }
}