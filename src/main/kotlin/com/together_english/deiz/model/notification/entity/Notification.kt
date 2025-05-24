package com.together_english.deiz.model.notification.entity

import com.together_english.deiz.common.base.BaseTimeEntity
import com.together_english.deiz.model.member.entity.Member
import com.together_english.deiz.model.notification.dto.NotificationDto
import jakarta.persistence.*
import java.util.*


@Entity
class Notification(
    member: Member,
    message: String
) : BaseTimeEntity() {

    @Id
    val id: UUID = UUID.randomUUID()

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    val member = member

    var viewed = false

    @Column(length = 400)
    val message = message

    fun toDto(): NotificationDto {
        return NotificationDto(
            id = this.id,
            viewed = this.viewed,
            message = this.message,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt
        )
    }
}