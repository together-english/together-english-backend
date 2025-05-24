package com.together_english.deiz.repository.custom

import com.querydsl.core.types.Projections
import com.together_english.deiz.model.circle.QCircle
import com.together_english.deiz.model.circle.QCircleJoinRequest
import com.together_english.deiz.model.circle.dto.CircleJoinRequestDto
import com.together_english.deiz.model.member.entity.QMember
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class CircleJoinRequestQueryDslRepository(
    private val queryFactory: JPAQueryFactory
) {
    fun findByMemberIdWithPagination(memberId: UUID, pageable: Pageable): Page<CircleJoinRequestDto> {
        val circleJoinRequest = QCircleJoinRequest.circleJoinRequest
        val circle = QCircle.circle
        val member = QMember.member

        val jpaQuery = queryFactory.select(
            Projections.constructor(
                CircleJoinRequestDto::class.java,
                circleJoinRequest.id,
                circleJoinRequest.member.id,
                circleJoinRequest.circle.id,
                circle.title,
                circleJoinRequest.status,
                circleJoinRequest.message,
                member.nickname,
                member.profile,
                circleJoinRequest.createdAt.stringValue(),
                circleJoinRequest.updatedAt.stringValue()
            )
        )
            .from(circleJoinRequest)
            .innerJoin(circleJoinRequest.circle, circle)
            .innerJoin(circleJoinRequest.member, member)
            .where(circle.leader.id.eq(memberId))
            .orderBy(circleJoinRequest.createdAt.desc())

        val results = jpaQuery
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        val total = queryFactory
            .select(circleJoinRequest.count())
            .from(circleJoinRequest)
            .where(circleJoinRequest.member.id.eq(memberId))
            .fetchOne() ?: 0L

        return PageImpl(results, pageable, total)
    }
}