package com.together_english.deiz.config

import com.together_english.deiz.model.notification.dto.NotificationEvent
import com.together_english.deiz.service.NotificationService
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class NotificationEventListener(
    private val notificationService: NotificationService
) {

    @EventListener
    fun onNotificationEvent(event: NotificationEvent) {
        notificationService.handleNotificationEvent(event)
    }
}