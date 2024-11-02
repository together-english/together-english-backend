package com.together_english.deiz.repository.custom

import com.together_english.deiz.model.circle.dto.CirclePageResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface CustomCircleRepository {
    fun findCirclesByPagination(pageable: Pageable): Page<CirclePageResponse?>
}