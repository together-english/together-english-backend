package com.together_english.deiz.repository.custom

import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.dsl.Expressions
import org.springframework.stereotype.Repository
import com.querydsl.jpa.impl.JPAQueryFactory
import com.together_english.deiz.model.circle.QCircle.circle
import com.together_english.deiz.model.circle.dto.CirclePageResponse
import com.together_english.deiz.model.circle.dto.CircleSearchRequest
import com.together_english.deiz.model.circle.dto.QCirclePageResponse
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

    private fun createDynamicCondition(request: CircleSearchRequest?) : BooleanBuilder {
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

        return builder
    }
}