package com.together_english.deiz.model.circle.dto

import com.together_english.deiz.model.circle.AttendMode
import com.together_english.deiz.model.circle.Circle
import com.together_english.deiz.model.circle.ContactWay
import com.together_english.deiz.model.common.City
import com.together_english.deiz.model.common.EnglishLevel
import com.together_english.deiz.model.member.entity.Member
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.springframework.web.multipart.MultipartFile

data class CircleCreateRequest (
        @NotBlank(message = "서클 이름은 필수 입력 사항입니다.")
        val name: String,

        @NotNull(message = "영어 레벨은 필수 입력 사항입니다.")
        val englishLevel: EnglishLevel,

        @NotNull(message = "도시는 필수 입력 사항입니다.")
        val city: City,

        @NotBlank(message = "소개는 필수 입력 사항입니다.")
        @Size(max = 3000, message = "소개는 3000자 이하로 작성해야 합니다.")
        val introduction: String,

        val address: String?,

        @NotNull(message = "정원은 필수 입력 사항입니다.")
        val capacity: Int,

        @NotNull(message = "참여 방식은 필수 입력 사항입니다.")
        val attendMode: AttendMode,

        @NotNull(message = "연락 방법은 필수 입력 사항입니다.")
        val contactWay: ContactWay,

        @NotNull(message = "스케줄은 최소 하나 이상 입력해야 합니다.")
        @Size(min = 1, message = "스케줄은 최소 하나 이상 입력해야 합니다.")
        val circleSchedules: List<CircleScheduleDto>,

)
{
    fun toEntity(leader: Member, thumbnailUrl: String?): Circle {
        return Circle(
                leader = leader,
                name = this.name,
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