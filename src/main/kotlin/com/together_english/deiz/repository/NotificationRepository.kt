package com.together_english.deiz.repository

import com.together_english.deiz.model.notification.entity.Notification
import com.together_english.deiz.repository.custom.CustomNotificationRepository
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime
import java.util.UUID

interface NotificationRepository: JpaRepository<Notification, UUID>, CustomNotificationRepository {

}