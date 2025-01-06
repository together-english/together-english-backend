package com.together_english.deiz.model.circle.dto

import com.together_english.deiz.model.circle.AttendMode
import com.together_english.deiz.model.circle.Circle
import com.together_english.deiz.model.common.City
import com.together_english.deiz.model.common.EnglishLevel
import com.together_english.deiz.model.member.entity.Member
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

@Schema(description = "Request DTO for creating a new Circle")
data class CircleCreateRequest (

        @field:NotBlank(message = "서클 이름은 필수 입력 사항입니다.")
        @Schema(description = "서클 이름", example = "영어 스터디 모임")
        val title: String,

        @field:NotNull(message = "영어 레벨은 필수 입력 사항입니다.")
        @Schema(description = "영어 레벨", example = "BEGINNER")
        val englishLevel: EnglishLevel,

        @field:NotNull(message = "도시는 필수 입력 사항입니다.")
        @Schema(description = "도시", example = "BUSAN")
        val city: City,

        @field:NotBlank(message = "소개는 필수 입력 사항입니다.")
        @field:Size(max = 3000, message = "소개는 3000자 이하로 작성해야 합니다.")
        @Schema(description = "서클 소개 (최대 3000자)", example = "이 서클은 영어 회화를 연습하기 위한 모임입니다.")
        val introduction: String,

        @Schema(description = "서클 주소 (선택 사항)", example = "서울 강남구 삼성로 100")
        val address: String?,

        @field:NotNull(message = "정원은 필수 입력 사항입니다.")
        @Schema(description = "서클 정원", example = "20")
        val capacity: Int,

        @field:NotNull(message = "참여 방식은 필수 입력 사항입니다.")
        @Schema(description = "참여 방식 (ONLINE 또는 OFFLINE)", example = "ONLINE")
        val attendMode: AttendMode,

        @field:NotNull(message = "연락 방법은 필수 입력 사항입니다.")
        @Schema(description = "연락 방법", example = "KAKAO_OPEN_CHAT")
        val contactWay: String,

        @field:NotNull(message = "스케줄은 최소 하나 이상 입력해야 합니다.")
        @field:Size(min = 1, message = "스케줄은 최소 하나 이상 입력해야 합니다.")
        @Schema(description = "서클 스케줄 목록 (최소 하나 이상)")
        val circleSchedules: List<CircleScheduleDto>
) {
    fun toEntity(leader: Member, thumbnailUrl: String?): Circle {
        return Circle(
                leader = leader,
                title = this.title,
                englishLevel = this.englishLevel,
                city = this.city,
                introduction = this.introduction,
                address = this.address,
                capacity = this.capacity,
                attendMode = this.attendMode,
                contactWay = this.contactWay,
                thumbnailUrl = thumbnailUrl
        )
    }
}
