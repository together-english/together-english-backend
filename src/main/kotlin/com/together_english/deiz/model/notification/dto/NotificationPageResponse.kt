package com.together_english.deiz.model.notification.dto

import java.time.LocalDateTime

data class NotificationPageResponse(
    val notifications: List<NotificationDto>,
    val lastCreatedAt: LocalDateTime?,
    val hasNext: Boolean
)