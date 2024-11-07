package com.together_english.deiz.service

import com.together_english.deiz.model.circle.dto.CommentCreateRequest
import com.together_english.deiz.model.circle.dto.CommentUpdateRequest
import com.together_english.deiz.model.member.entity.Member
import com.together_english.deiz.repository.CircleRepository
import com.together_english.deiz.repository.CommentRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class CommentService(
    private val commentRepository: CommentRepository,
    private val circleRepository: CircleRepository,
) {
    @Transactional
    fun createCircleComment(request: CommentCreateRequest, member: Member): String {
        require(request.content.length > 2) { "댓글은 2글자 이상으로 작성해야합니다." }

        val circle = circleRepository.findById(request.circleId)
            .orElseThrow { NoSuchElementException("circle id : ${request.circleId} not found") }

        val comment = request.toEntity(circle, member)
        commentRepository.save(comment)
        return comment.id.toString()
    }

    @Transactional
    fun updateCircleComment(request: CommentUpdateRequest, member: Member): String {
        require(request.content.length > 2) { "댓글은 2글자 이상으로 작성해야합니다." }

        val comment = commentRepository.findById(request.commentId)
            .orElseThrow { NoSuchElementException("comment id : ${request.commentId} not found") }

        require(comment.isWritten(member)) { "댓글 작성자만 수정 가능합니다." }
        comment.updateContent(request)
        return comment.id.toString()
    }
}