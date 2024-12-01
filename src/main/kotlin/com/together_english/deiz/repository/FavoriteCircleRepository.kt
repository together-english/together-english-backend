package com.together_english.deiz.repository

import com.together_english.deiz.model.circle.Circle
import com.together_english.deiz.model.circle.FavoriteCircle
import com.together_english.deiz.model.circle.FavoriteCircleId
import com.together_english.deiz.model.member.entity.Member
import org.springframework.data.jpa.repository.JpaRepository

interface FavoriteCircleRepository: JpaRepository<FavoriteCircle, FavoriteCircleId> {
    fun findByCircleAndMember(circle: Circle, member: Member): FavoriteCircle?
}