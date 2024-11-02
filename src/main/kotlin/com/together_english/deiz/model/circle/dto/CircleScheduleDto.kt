package com.together_english.deiz.model.circle.dto

import com.together_english.deiz.model.circle.Circle
import com.together_english.deiz.model.circle.CircleSchedule
import com.together_english.deiz.model.common.DayOfWeek
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

@Schema(description = "DTO for Circle Schedule details")
data class CircleScheduleDto(

        @field:NotBlank(message = "요일은 필수 입력 사항입니다.")
        @Schema(description = "요일", example = "MONDAY")
        val dayOfWeek: DayOfWeek,

        @field:NotBlank(message = "시작 시간은 필수 입력 사항입니다.")
        @Schema(description = "시작 시간 (HH:mm 형식)", example = "10:00")
        val startTime: String,

        @field:NotBlank(message = "종료 시간은 필수 입력 사항입니다.")
        @Schema(description = "종료 시간 (HH:mm 형식)", example = "12:00")
        val endTime: String
) {
        fun toEntity(dto: CircleScheduleDto, circle: Circle): CircleSchedule {
                return CircleSchedule(
                        circle = circle,
                        dayOfWeek = dayOfWeek,
                        startTime = startTime,
                        endTime = endTime
                )
        }
}
