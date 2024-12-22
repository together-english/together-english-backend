package com.together_english.deiz.repository.custom

import com.together_english.deiz.model.circle.dto.CircleMemberDetailResponse
import com.together_english.deiz.model.circle.dto.CircleMemberPageResponse
import com.together_english.deiz.model.member.dto.MyJoinedCirclePageResponse
import com.together_english.deiz.model.member.entity.Member
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface CustomCircleMemberRepository {
    fun findMemberByCircle(circleId: UUID, pageable: Pageable): Page<CircleMemberPageResponse?>
    fun findMemberDetailsByCircle(circleMemberId: UUID): CircleMemberDetailResponse
    fun findCircleByMember(member: Member, pageable: Pageable): Page<MyJoinedCirclePageResponse?>
}