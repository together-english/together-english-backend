package com.together_english.deiz.model.member.dto

import com.together_english.deiz.model.common.City
import com.together_english.deiz.model.common.EnglishLevel
import io.swagger.v3.oas.annotations.media.Schema
import java.util.*

@Schema(description = "내가 창설한 모임 리스트 조회 응답")
data class MyCreatedCirclePageResponse(
    @Schema(description = "서클 ID", example = "FWEHFEWD32532332")
    val id: UUID,
    @Schema(description = "썸네일 URL", example = "thumbnail.link.com")
    val thumbnailUrl: String? = null,
    @Schema(description = "서클 제목", example = "영어 스터디 그룹")
    val title: String,
    @Schema(description = "서클 소개", example = "영어 회화 능력을 함께 키워가는 모임입니다.")
    val introduction: String = "",
    @Schema(description = "리더 프로필 URL", example = "leader.profile.link.com")
    val leaderProfile: String? = null,
    @Schema(description = "리더 이름", example = "홍길동")
    val leaderName: String,
    @Schema(description = "영어 수준", example = "INTERMEDIATE")
    val englishLevel: EnglishLevel,
    @Schema(description = "도시", example = "SEOUL")
    val city: City,
    @Schema(description = "참여 인원 수", example = "20")
    val capacity: Int,
    @Schema(description = "총 조회 수", example = "150")
    val totalView: Int,
    @Schema(description = "총 좋아요 수", example = "30")
    val totalLike: Long,
    @Schema(description = "내가 좋아요를 눌렀는지 여부", example = "false")
    val likedByMe: Boolean? = false
) {

}