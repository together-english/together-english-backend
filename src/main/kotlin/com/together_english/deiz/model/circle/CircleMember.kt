package com.together_english.deiz.model.circle

import com.together_english.deiz.common.base.BaseTimeEntity
import com.together_english.deiz.model.member.entity.Member
import jakarta.persistence.*
import java.util.*

@Entity
@Table(
    name = "circle_member",
    indexes = [
        Index(name = "idx_circle_member_circle_member", columnList = "circle_id, member_id")
    ]
)
class CircleMember(
    member: Member,
    circle: Circle,
) : BaseTimeEntity() {
    @Id
    val id: UUID = UUID.randomUUID()

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "circle_id")
    val circle: Circle = circle

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "member_id")
    val member: Member = member

    @Enumerated(EnumType.STRING)
    @Column(name = "role", length = 50, nullable = false)
    var role: CircleRole = CircleRole.MEMBER
    private set

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 12, nullable = false)
    var status: CircleMemberStatus = CircleMemberStatus.NORMAL
    private set

    fun updateRole(newRole: CircleRole) {
        this.role = newRole
    }

    fun updateStatus(newStatus: CircleMemberStatus) {
        this.status = newStatus
    }

    enum class CircleRole {
        LEADER, MEMBER
    }

    enum class CircleMemberStatus {
        NORMAL, BANNED
    }
}

