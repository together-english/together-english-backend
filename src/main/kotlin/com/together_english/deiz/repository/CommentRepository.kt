package com.together_english.deiz.repository

import com.together_english.deiz.model.comment.CircleComment
import com.together_english.deiz.model.comment.CommentStatus
import com.together_english.deiz.model.comment.dto.CommentPageResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface CommentRepository : JpaRepository<CircleComment, UUID> {
    fun findByCircleIdAndStatus(pageable: Pageable,circleId: UUID, status: CommentStatus): Page<CommentPageResponse?>
}