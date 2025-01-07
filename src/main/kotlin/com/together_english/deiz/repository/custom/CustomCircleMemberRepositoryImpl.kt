package com.together_english.deiz.repository.custom

import com.linecorp.kotlinjdsl.support.spring.data.jpa.repository.KotlinJdslJpqlExecutor
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.impl.JPAQueryFactory
import com.together_english.deiz.model.circle.Circle
import com.together_english.deiz.model.circle.CircleMember
import com.together_english.deiz.model.circle.QCircle.circle
import com.together_english.deiz.model.circle.QCircleMember.circleMember
import com.together_english.deiz.model.circle.QFavoriteCircle.favoriteCircle
import com.together_english.deiz.model.circle.dto.CircleMemberDetailResponse
import com.together_english.deiz.model.circle.dto.CircleMemberPageResponse
import com.together_english.deiz.model.member.dto.MyJoinedCirclePageResponse
import com.together_english.deiz.model.member.dto.QMyJoinedCirclePageResponse
import com.together_english.deiz.model.member.entity.Member
import com.together_english.deiz.model.member.entity.QMember
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import java.util.*

class CustomCircleMemberRepositoryImpl(
    private val kotlinJdslJpqlExecutor: KotlinJdslJpqlExecutor,
    private val queryFactory: JPAQueryFactory
) : CustomCircleMemberRepository {

    override fun findMemberByCircle(circleId: UUID, pageable: Pageable): Page<CircleMemberPageResponse?> {
        val result =
            kotlinJdslJpqlExecutor.findPage(pageable) {
                selectNew<CircleMemberPageResponse>(
                    path(CircleMember::id).`as`(expression("circleMemberId")),
                    path(Member::nickname),
                    path(Member::profile),
                    path(CircleMember::role),
                ).from(
                    entity(CircleMember::class),
                    join(CircleMember::circle),
                    join(CircleMember::member),
                ).whereAnd(
                    path(CircleMember::circle).path(Circle::id).eq(circleId),
                    path(Circle::valid).eq(true),
                    path(Member::valid).eq(true)
                )
            }
        return result
    }

    override fun findMemberDetailsByCircle(circleMemberId: UUID): CircleMemberDetailResponse {
        val result =
            kotlinJdslJpqlExecutor.findAll {
                selectNew<CircleMemberDetailResponse>(
                    path(CircleMember::circle).path(Circle::id).`as`(expression("circleId")),
                    path(Member::nickname),
                    path(Member::email),
                    path(Member::profile),
                    path(Member::gender),
                    path(Member::age),
                    path(CircleMember::role),
                ).from(
                    entity(CircleMember::class),
                    join(CircleMember::circle),
                    join(CircleMember::member),
                ).whereAnd(
                    path(CircleMember::id).eq(circleMemberId),
                    path(Circle::valid).eq(true),
                    path(Member::valid).eq(true)
                )
            }.single()
        return result!!
    }

    override fun findCircleByMember(member: Member, pageable: Pageable): Page<MyJoinedCirclePageResponse?> {
        val isLikedByMe =
            Expressions.booleanTemplate("case when {0} is not null then true else false end", favoriteCircle)
        val jpaQuery = queryFactory.select(
            QMyJoinedCirclePageResponse(
                circle.id.`as`("circleId"),
                circleMember.id.`as`("circleMemberId"),
                circle.thumbnailUrl,
                circle.title,
                circle.introduction,
                QMember.member.profile.`as`("leaderProfile"),
                QMember.member.nickname.`as`("leaderName"),
                circle.englishLevel,
                circle.city,
                circle.capacity,
                circle.totalView,
                circle.totalLike,
                isLikedByMe
            )
        ).from(circleMember)
            .join(circle)
            .on(circleMember.circle.id.eq(circle.id))
            .join(QMember.member)
            .on(circleMember.member.id.eq(member.id))
            .leftJoin(favoriteCircle)
            .on(
                favoriteCircle.circle.id.eq(circle.id)
                    .and(favoriteCircle.member.id.eq(member.id))
            )
            .where(
                QMember.member.id.eq(member.id)
                    .and(circleMember.status.eq(CircleMember.CircleMemberStatus.NORMAL))
                    .and(circle.valid)
                    .and(QMember.member.valid)
            )

        val results = jpaQuery.offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        return PageImpl(results, pageable, jpaQuery.fetchCount())
    }
}