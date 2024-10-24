package com.together_english.deiz.model.circle

import com.together_english.deiz.common.base.BaseEntity
import com.together_english.deiz.model.member.entity.Member
import jakarta.persistence.*
import org.jetbrains.annotations.NotNull

@Entity
class CircleComment(
    content : String,
    circle : Circle,
    member : Member,
) : BaseEntity() {

    @Column(length = 255)
    @NotNull
    var content: String = content

    @Column(length = 50)
    @Enumerated(value = EnumType.STRING)
    @NotNull
    var status: CommentStatus = CommentStatus.REPORTED

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "circle_id")
    @NotNull
    val circle : Circle = circle


    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "member_id")
    @NotNull
    val member : Member = member

    fun deleteComment() {
        status = CommentStatus.DELETED
    }
}

