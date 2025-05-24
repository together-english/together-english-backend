package com.together_english.deiz.repository.custom

import com.querydsl.jpa.impl.JPAQueryFactory
import com.together_english.deiz.model.member.entity.Member
import com.together_english.deiz.model.notification.entity.Notification
import org.springframework.stereotype.Repository
import com.together_english.deiz.model.notification.entity.QNotification.notification
import java.time.LocalDateTime

@Repository
class CustomNotificationRepositoryImpl(
    private val queryFactory: JPAQueryFactory
): CustomNotificationRepository {
    override fun findByMemberAndViewedAfterCreatedAt(
        member: Member,
        viewed: Boolean?,
        lastCreatedAt: LocalDateTime?,
        size: Int
    ): List<Notification> {
        val query = queryFactory
            .selectFrom(notification)
            .where(
                notification.member.eq(member),
                viewed?.let { notification.viewed.eq(it) },
                lastCreatedAt?.let { notification.createdAt.lt(it) }
            )
            .orderBy(notification.createdAt.desc())
            .limit((size + 1).toLong())

        return query.fetch()
    }

}