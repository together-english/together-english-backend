package com.together_english.deiz.repository.custom

import com.together_english.deiz.model.circle.Circle
import com.together_english.deiz.model.circle.dto.CirclePageResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

interface CustomCircleRepository {
    fun findCirclesWithSchedules(pageable: Pageable): Page<CirclePageResponse?>
}