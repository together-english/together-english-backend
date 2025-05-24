package com.together_english.deiz.repository.custom

import com.together_english.deiz.model.member.entity.Member
import com.together_english.deiz.model.notification.entity.Notification
import java.time.LocalDateTime

interface CustomNotificationRepository {
    fun findByMemberAndViewedAfterCreatedAt(
        member: Member,
        viewed: Boolean?,
        lastCreatedAt: LocalDateTime?,
        size: Int
    ): List<Notification>
}