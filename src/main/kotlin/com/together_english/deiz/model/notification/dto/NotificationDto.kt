package com.together_english.deiz.model.notification.dto

import java.time.LocalDateTime
import java.util.*


data class NotificationDto(
    val id: UUID,
    val viewed: Boolean,
    val message: String,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?
)