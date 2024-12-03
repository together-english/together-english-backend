package com.together_english.deiz.model.circle.dto

import com.together_english.deiz.model.common.City
import com.together_english.deiz.model.common.EnglishLevel
import com.together_english.deiz.model.member.entity.Member
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Size
import java.util.UUID

@Schema(description = "DTO for searching Circles with optional filters")
data class CircleSearchRequest(
        @field:Schema(description = "사용자ID", example = "FWEHFEWD32532332")
        val memberId: UUID? = null,
        @field:Schema(description = "검색할 제목", example = "English Conversation Group")
        @field:Size(max = 100, message = "제목은 최대 100자까지 입력할 수 있습니다.")
        val title: String?,
        @field:Schema(description = "도시 필터", example = "SEOUL")
        val city: City?,
        @field:Schema(description = "영어 수준 필터", example = "BEGINNER")
        val level: EnglishLevel?,
        @field:Schema(description = "내가 찜한 모임만 조회 여부", example = "true/false")
        val likeByMeOnly: Boolean? = false,
)