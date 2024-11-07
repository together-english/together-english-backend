package com.together_english.deiz.model.circle.dto

import com.together_english.deiz.model.circle.Circle
import com.together_english.deiz.model.circle.CircleComment
import com.together_english.deiz.model.member.entity.Member
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import java.util.*

@Schema(name = "댓글 수정 요청")
data class CommentUpdateRequest(
    @NotBlank(message = "댓글 내용은 필수 입력 사항입니다.")
    @Schema(description = "댓글 내용", example = "테스트 댓글")
    val content: String,

    @Schema(description = "댓글 ID", example = "0192b531-1c11-b892-8814-868f503bf0f4")
    val commentId: UUID,
) {
    fun toEntity(circle: Circle, member: Member): CircleComment {
        return CircleComment(
            content = this.content,
            member = member,
            circle = circle,
        )
    }
}