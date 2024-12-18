package com.together_english.deiz.model.circle

import com.together_english.deiz.common.base.BaseTimeEntity
import com.together_english.deiz.model.circle.dto.CircleJoinDetailResponse
import com.together_english.deiz.model.circle.dto.CircleJoinUpdateRequestDTO
import com.together_english.deiz.model.member.entity.Member
import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(
    name = "circle_join_request",
    uniqueConstraints = [
        UniqueConstraint(name = "uk_circle_member", columnNames = ["circle_id", "member_id"])
    ]
)
class CircleJoinRequest(
    circle: Circle,
    member: Member,
    message: String,
) : BaseTimeEntity(){

    @Id
    val id: UUID = UUID.randomUUID()

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "circle_id", nullable = false)
    val circle: Circle = circle

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    val member: Member = member

    @Column(name = "message", length = 3000)
    var message: String = message

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 32, nullable = false)
    var status: CircleJoinStatus = CircleJoinStatus.WAITING

    fun updateMessage(request: CircleJoinUpdateRequestDTO) {
        this.message = request.message
    }

    fun isWrittenBy(member: Member): Boolean {
        return this.member.id == member.id
    }

    fun toCircleJoinDetailResponse(): CircleJoinDetailResponse {
        return CircleJoinDetailResponse(
            circleJoinRequestId = this.id,
            nickname = this.member.nickname,
            profile = this.member.profile,
            gender = this.member.gender,
            age = this.member.age,
            message = this.message,
            status = this.status
        )
    }
}