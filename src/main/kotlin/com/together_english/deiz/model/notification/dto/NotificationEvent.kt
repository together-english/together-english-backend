package com.together_english.deiz.model.notification.dto

import java.util.*

data class NotificationEvent(
    val memberId: UUID,
    val message: String
)