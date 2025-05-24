package com.together_english.deiz.service

import com.together_english.deiz.exception.NotExistException
import com.together_english.deiz.model.member.entity.Member
import com.together_english.deiz.model.notification.dto.NotificationEvent
import com.together_english.deiz.model.notification.dto.NotificationPageResponse
import com.together_english.deiz.model.notification.entity.Notification
import com.together_english.deiz.repository.MemberRepository
import com.together_english.deiz.repository.NotificationRepository
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

@Service
class NotificationService(
    private val notificationRepository: NotificationRepository,
    private val eventPublisher: ApplicationEventPublisher,
    private val memberRepository: MemberRepository
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    fun publishNotification(memberId: UUID, message: String) {
        eventPublisher.publishEvent(NotificationEvent(memberId, message))
    }

    @Async("taskExecutor")
    @Transactional
    fun handleNotificationEvent(event: NotificationEvent) {
        try {
            val member = memberRepository.findById(event.memberId)
                .orElseThrow { NotExistException("member id : ${event.memberId}") }
            val notification = Notification(
                member = member,
                message = event.message
            )
            notificationRepository.save(notification)
            logger.info("Notification saved for memberId: ${event.memberId}, message: ${event.message}")
        } catch (e: Exception) {
            logger.error("Failed to save notification for memberId: ${event.memberId}", e)
        }
    }

    @Transactional
    fun findNotificationsByMemberIdAfterCreatedAt(
        member: Member,
        viewed: Boolean?,
        lastCreatedAt: LocalDateTime?,
        size: Int
    ): NotificationPageResponse {
        val notifications = notificationRepository.findByMemberAndViewedAfterCreatedAt(
            member, viewed, lastCreatedAt, size
        )
        val hasNext = notifications.size > size
        val result = if (hasNext) notifications.dropLast(1) else notifications
        val nextLastCreatedAt = result.lastOrNull()?.createdAt

        val notificationIds = result.filter { !it.viewed }.map { it.id }
        if (notificationIds.isNotEmpty()) {
            updateViewedAsync(notificationIds)
            logger.debug("Triggered async viewed update for notifications: $notificationIds")
        } else {
            logger.debug("No notifications to update viewed status")
        }

        return NotificationPageResponse(
            notifications = result.map { it.toDto() },
            lastCreatedAt = nextLastCreatedAt,
            hasNext = hasNext
        )
    }

    @Async("taskExecutor")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun updateViewedAsync(notificationIds: List<UUID>) {
        try {
            val now = LocalDateTime.now()
            val updatedCount = notificationRepository.updateViewedByIds(notificationIds, now)
            logger.info("Async updated viewed to true for $updatedCount notifications: $notificationIds")
        } catch (e: Exception) {
            logger.error("Failed to async update viewed for notifications: $notificationIds", e)
        }
    }


}