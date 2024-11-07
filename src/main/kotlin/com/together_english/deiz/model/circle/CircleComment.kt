package com.together_english.deiz.model.circle

import com.together_english.deiz.common.base.BaseTimeEntity
import com.together_english.deiz.model.circle.dto.CommentUpdateRequest
import com.together_english.deiz.model.member.entity.Member
import jakarta.persistence.*
import org.jetbrains.annotations.NotNull
import java.util.*

@Entity
class CircleComment(
    content : String,
    circle : Circle,
    member : Member,
) : BaseTimeEntity() {

    @Id
    val id: UUID = UUID.randomUUID()

    @Column(length = 255)
    @NotNull
    var content: String = content
    private set

    @Column(length = 50)
    @Enumerated(value = EnumType.STRING)
    @NotNull
    var status: CommentStatus = CommentStatus.REPORTED
    private set

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "circle_id")
    @NotNull
    val circle : Circle = circle

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "member_id")
    @NotNull
    val member : Member = member

    fun isWrittenBy(member: Member): Boolean {
        return this.member.id == member.id
    }

    fun updateContent(request: CommentUpdateRequest) {
        this.content = request.content
    }

    fun deleteComment() {
        this.status = CommentStatus.DELETED
    }
}

