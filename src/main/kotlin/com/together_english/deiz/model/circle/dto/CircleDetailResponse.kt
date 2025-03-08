package com.together_english.deiz.model.circle.dto

import com.together_english.deiz.model.circle.AttendMode
import com.together_english.deiz.model.circle.Circle
import com.together_english.deiz.model.circle.CircleStatus
import com.together_english.deiz.model.common.City
import com.together_english.deiz.model.common.EnglishLevel
import java.util.*

data class CircleDetailResponse(
        val id: UUID,
        val title: String,
        val leaderProfile: String? = null,
        val leaderName: String,
        val englishLevel: EnglishLevel,
        val city: City,
        val thumbnail: String? = null,
        val introduction: String,
        val address: String? = null,
        val capacity: Int,
        val circleStatus: CircleStatus,
        val attendMode: AttendMode,
        val contactWay: String?,
        val onlineUrl: String? = null,
        val totalView: Int = 0,
        val weekView: Int = 0,
        val circleSchedules: List<CircleScheduleDto>
) {
    companion object {
        fun fromEntity(circle: Circle): CircleDetailResponse {
            return CircleDetailResponse(
                    id = circle.id,
                    title = circle.title,
                    englishLevel = circle.englishLevel,
                    city = circle.city,
                    leaderProfile = circle.leader.profile,
                    leaderName = circle.leader.nickname,
                    thumbnail = circle.thumbnailUrl,
                    introduction = circle.introduction,
                    address = circle.address,
                    capacity = circle.capacity,
                    circleStatus = circle.circleStatus,
                    attendMode = circle.attendMode,
                    contactWay = circle.contactWay,
                    onlineUrl = circle.onlineUrl,
                    totalView = circle.totalView,
                    weekView = circle.weekView,
                    circleSchedules = circle.circleSchedules.map {
                            CircleScheduleDto(
                                    dayOfWeek = it.dayOfWeek,
                                    startTime = it.startTime,
                                    endTime = it.endTime
                            )
                    }
            )
        }
    }
}