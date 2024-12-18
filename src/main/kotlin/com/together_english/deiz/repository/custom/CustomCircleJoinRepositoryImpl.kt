package com.together_english.deiz.repository.custom

import com.linecorp.kotlinjdsl.support.spring.data.jpa.repository.KotlinJdslJpqlExecutor
import com.together_english.deiz.model.circle.CircleJoinRequest
import com.together_english.deiz.model.circle.dto.CircleJoinDetailResponse
import com.together_english.deiz.model.member.entity.Member
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class CustomCircleJoinRepositoryImpl(
    private val kotlinJdslJpqlExecutor: KotlinJdslJpqlExecutor,
) : CustomCircleJoinRepository {

    override fun findCircleJoinRequestDetail(circleJoinRequestId: UUID): CircleJoinDetailResponse? {
        val result =
            kotlinJdslJpqlExecutor.findAll {
            selectNew<CircleJoinDetailResponse>(
                path(CircleJoinRequest::id).`as`(expression("circleJoinRequestId")),
                path(Member::nickname),
                path(Member::profile),
                path(Member::gender),
                path(Member::age),
                path(CircleJoinRequest::message),
                path(CircleJoinRequest::status),
            ).from(
                entity(CircleJoinRequest::class),
                join(CircleJoinRequest::circle),
                join(CircleJoinRequest::member),
            ).whereAnd(
                path(CircleJoinRequest::id).eq(circleJoinRequestId),
            )
        }.single()
        return result
    }
}