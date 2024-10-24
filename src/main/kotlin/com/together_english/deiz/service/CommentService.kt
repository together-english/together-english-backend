package com.together_english.deiz.service

import com.together_english.deiz.model.circle.dto.CommentCreateRequest
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
    fun createComment(request: CommentCreateRequest, member: Member): String {
        val circle = circleRepository.findById(request.circleId)
            .orElseThrow { NoSuchElementException("circle id : ${request.circleId} not found") }

        val comment = request.toEntity(circle, member)
        commentRepository.save(comment)
        return comment.id.toString()
    }
}