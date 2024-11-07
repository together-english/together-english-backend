package com.together_english.deiz.repository

import com.together_english.deiz.model.circle.CircleComment
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface CommentRepository : JpaRepository<CircleComment, UUID> {
}