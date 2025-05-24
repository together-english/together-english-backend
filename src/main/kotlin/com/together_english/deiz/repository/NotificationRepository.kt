package com.together_english.deiz.repository

import com.together_english.deiz.model.notification.entity.Notification
import com.together_english.deiz.repository.custom.CustomNotificationRepository
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime
import java.util.UUID
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface NotificationRepository: JpaRepository<Notification, UUID>, CustomNotificationRepository {
    @Modifying
    @Query(
        "UPDATE Notification n SET n.viewed = true, n.updatedAt = :now " +
                "WHERE n.id IN :ids AND n.viewed = false"
    )
    fun updateViewedByIds(
        @Param("ids") ids: List<UUID>,
        @Param("now") now: LocalDateTime
    ): Int
}