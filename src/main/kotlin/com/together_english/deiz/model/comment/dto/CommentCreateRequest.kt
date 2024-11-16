package com.together_english.deiz.model.comment.dto

import com.together_english.deiz.model.circle.Circle
import com.together_english.deiz.model.comment.CircleComment
import com.together_english.deiz.model.member.entity.Member
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import java.util.UUID

@Schema(description = "댓글 생성 요청")
data class CommentCreateRequest(
    @NotBlank(message = "댓글 내용은 필수 입력 사항입니다.")
    @Schema(description = "댓글 내용", example = "테스트 댓글")
    val content: String,

    @Schema(description = "영어모임 ID", example = "0192b531-1c11-b892-8814-868f503bf0f4")
    val circleId: UUID,

    ) {
    fun toEntity(circle: Circle, member: Member): CircleComment {
        return CircleComment(
            content = this.content,
            member = member,
            circle = circle,
        )
    }
}