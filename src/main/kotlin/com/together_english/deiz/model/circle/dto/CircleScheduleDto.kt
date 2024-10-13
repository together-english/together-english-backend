package com.together_english.deiz.model.circle.dto

import com.together_english.deiz.model.circle.Circle
import com.together_english.deiz.model.circle.CircleSchedule
import com.together_english.deiz.model.common.DayOfWeek
import jakarta.validation.constraints.NotBlank

data class CircleScheduleDto(
        @NotBlank(message = "요일은 필수 입력 사항입니다.")
        val dayOfWeek: DayOfWeek,

        @NotBlank(message = "시작 시간은 필수 입력 사항입니다.")
        val startTime: String,

        @NotBlank(message = "종료 시간은 필수 입력 사항입니다.")
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