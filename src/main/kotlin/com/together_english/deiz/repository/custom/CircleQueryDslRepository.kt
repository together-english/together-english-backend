package com.together_english.deiz.repository.custom

import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.dsl.Expressions
import org.springframework.stereotype.Repository
import com.querydsl.jpa.impl.JPAQueryFactory
import com.together_english.deiz.model.circle.QCircle.circle
import com.together_english.deiz.model.circle.QFavoriteCircle.favoriteCircle
import com.together_english.deiz.model.circle.dto.CirclePageResponse
import com.together_english.deiz.model.circle.dto.CircleSearchRequest
import com.together_english.deiz.model.circle.dto.QCirclePageResponse
import com.together_english.deiz.model.member.dto.MyCreatedCirclePageResponse
import com.together_english.deiz.model.member.dto.QMyCreatedCirclePageResponse
import com.together_english.deiz.model.member.entity.Member
import com.together_english.deiz.model.member.entity.QMember.member
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable


@Repository
class CircleQueryDslRepository(
    private val queryFactory: JPAQueryFactory
) {
    fun searchPageForAnonymous(pageable: Pageable, request: CircleSearchRequest?): Page<CirclePageResponse> {
        val builder = createDynamicCondition(request)
        val jpaQuery = queryFactory.select(
            QCirclePageResponse(
                circle.id,
                circle.thumbnailUrl,
                circle.title,
                circle.introduction,
                member.profile,
                member.nickname,
                circle.englishLevel,
                circle.city,
                circle.capacity,
                circle.totalView,
                circle.totalLike,
                Expressions.asBoolean(false)
            )
        ).from(circle)
            .innerJoin(member).on(circle.leader.eq(member))
            .where(builder)
            .orderBy(circle.createdAt.desc())

        val results = jpaQuery.offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        return PageImpl(results, pageable, jpaQuery.fetchCount());
    }

    fun searchPageForMember(pageable: Pageable, request: CircleSearchRequest?): Page<CirclePageResponse> {
        val builder = createDynamicCondition(request)
        val isLikedByMe =
            Expressions.booleanTemplate("case when {0} is not null then true else false end", favoriteCircle)
        val jpaQuery = queryFactory.select(
            QCirclePageResponse(
                circle.id,
                circle.thumbnailUrl,
                circle.title,
                circle.introduction,
                member.profile,
                member.nickname,
                circle.englishLevel,
                circle.city,
                circle.capacity,
                circle.totalView,
                circle.totalLike,
                isLikedByMe
            )
        ).from(circle)
            .innerJoin(member).on(circle.leader.eq(member))
            .leftJoin(favoriteCircle)
            .on(
                favoriteCircle.circle.id.eq(circle.id)
                    .and(favoriteCircle.member.id.eq(request!!.memberId))
            )
            .where(builder)
            .orderBy(circle.createdAt.desc())

        val results = jpaQuery.offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        return PageImpl(results, pageable, jpaQuery.fetchCount());
    }

    fun findCreatedCirclesByPagination(member: Member, pageable: Pageable): Page<MyCreatedCirclePageResponse?> {
        val isLikedByMe =
            Expressions.booleanTemplate("case when {0} is not null then true else false end", favoriteCircle)
        val jpaQuery = queryFactory.select(
            QMyCreatedCirclePageResponse(
                circle.id,
                circle.thumbnailUrl,
                circle.title,
                circle.introduction,
                circle.leader.profile.`as`("leaderProfile"),
                circle.leader.nickname.`as`("leaderName"),
                circle.englishLevel,
                circle.city,
                circle.capacity,
                circle.totalView,
                circle.totalLike,
                isLikedByMe
            )
        ).from(circle)
            .join(circle.leader)
            .on(circle.leader.id.eq(member.id))
            .leftJoin(favoriteCircle)
            .on(
                favoriteCircle.circle.id.eq(circle.id)
                    .and(favoriteCircle.member.id.eq(member.id))
            )
            .where(
                circle.valid
                    .and(circle.leader.valid)
            )
            .orderBy(circle.createdAt.desc())

        val results = jpaQuery.offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        return PageImpl(results, pageable, jpaQuery.fetchCount())
    }

    private fun createDynamicCondition(request: CircleSearchRequest?): BooleanBuilder {
        val builder = BooleanBuilder()

        request?.title?.let {
            builder.and(circle.title.contains(request.title))
        }
        request?.city?.let {
            builder.and(circle.city.eq(request.city))
        }
        request?.level?.let {
            builder.and(circle.englishLevel.eq(request.level))
        }
        request?.likeByMeOnly?.let {
            if (request.likeByMeOnly) {
                builder.and(favoriteCircle.member.id.eq(request.memberId))
            }
        }

        return builder
    }
}