package com.together_english.deiz.model.circle

import com.together_english.deiz.model.member.entity.Member
import jakarta.persistence.*

@Entity
@IdClass(FavoriteCircleId::class)
data class FavoriteCircle(
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "circle_id", nullable = false)
    val circle: Circle,

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    val member: Member
)
