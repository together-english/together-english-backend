package com.together_english.deiz.model.circle.dto

import com.together_english.deiz.model.circle.AttendMode
import com.together_english.deiz.model.circle.Circle
import com.together_english.deiz.model.circle.ContactWay
import com.together_english.deiz.model.common.City
import com.together_english.deiz.model.common.EnglishLevel
import com.together_english.deiz.model.member.entity.Member

data class CircleRequestDTO(
        val leaderId: Long,
        val name: String,
        val englishLevel: EnglishLevel,
        val city: City,
        val thumbnail: String? = null,
        val introduction: String,
        val address: String? = null,
        val capacity: Int,
        val attendMode: AttendMode,
        val contactWay: ContactWay,
        val onlineUrl: String? = null
) {
    fun toEntity(request: CircleRequestDTO, leader: Member): Circle {
        return Circle(
                leader = leader,
                name = name,
                englishLevel = englishLevel,
                city = city,
                thumbnail = thumbnail,
                introduction = introduction,
                address = address,
                capacity = capacity,
                attendMode = attendMode,
                contactWay = contactWay,
                onlineUrl = onlineUrl
        )
    }
}
