package com.together_english.deiz.repository

import com.together_english.deiz.model.circle.Circle
import com.together_english.deiz.model.circle.CircleJoinRequest
import com.together_english.deiz.model.circle.CircleJoinStatus
import com.together_english.deiz.repository.custom.CustomCircleJoinRepository
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface CircleJoinRepository: JpaRepository<CircleJoinRequest, UUID> , CustomCircleJoinRepository{
    fun findByCircle(circle: Circle): List<CircleJoinRequest>
    fun findByCircleIdAndMemberIdAndStatusIs(
        circleId: UUID,
        memberId: UUID,
        status: CircleJoinStatus
    ): CircleJoinRequest
    fun findByCircleIdAndMemberIdAndStatusIsNot(
        circleId: UUID,
        memberId: UUID,
        status: CircleJoinStatus
    ): CircleJoinRequest?
}