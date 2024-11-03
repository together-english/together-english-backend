package com.together_english.deiz.model.circle.dto

import com.together_english.deiz.model.circle.AttendMode
import com.together_english.deiz.model.circle.Circle
import com.together_english.deiz.model.circle.CircleStatus
import com.together_english.deiz.model.circle.ContactWay
import com.together_english.deiz.model.common.City
import com.together_english.deiz.model.common.EnglishLevel
import java.util.*

data class CircleDetailResponse(
        val id: UUID,
        val title: String,
        val englishLevel: EnglishLevel,
        val city: City,
        val thumbnail: String? = null,
        val introduction: String,
        val address: String? = null,
        val capacity: Int,
        val circleStatus: CircleStatus,
        val attendMode: AttendMode,
        val contactWay: ContactWay,
        val onlineUrl: String? = null,
        val totalView: Int = 0,
        val weekView: Int = 0
) {
    companion object {
        fun fromEntity(circle: Circle): CircleDetailResponse {
            return CircleDetailResponse(
                    id = circle.id,
                    title = circle.title,
                    englishLevel = circle.englishLevel,
                    city = circle.city,
                    thumbnail = circle.thumbnailUrl,
                    introduction = circle.introduction,
                    address = circle.address,
                    capacity = circle.capacity.toInt(),
                    circleStatus = circle.circleStatus,
                    attendMode = circle.attendMode,
                    contactWay = circle.contactWay,
                    onlineUrl = circle.onlineUrl,
                    totalView = circle.totalView,
                    weekView = circle.weekView
            )
        }
    }
}