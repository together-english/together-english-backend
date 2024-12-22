package com.together_english.deiz.repository

import com.together_english.deiz.model.circle.Circle
import com.together_english.deiz.model.circle.CircleMember
import com.together_english.deiz.model.member.entity.Member
import com.together_english.deiz.repository.custom.CustomCircleMemberRepository
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface CircleMemberRepository: JpaRepository<CircleMember, UUID>, CustomCircleMemberRepository {
    fun findByCircleAndMember(circle: Circle, member: Member): Optional<CircleMember>
    fun existsByCircleIdAndMemberId(circleId: UUID, memberId: UUID): Boolean
}