package com.together_english.deiz.repository.custom

import com.together_english.deiz.model.circle.dto.CirclePageResponse
import com.together_english.deiz.model.circle.dto.CircleSearchRequest
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface CustomCircleRepository {
    fun findCirclesByPagination(pageable: Pageable, request: CircleSearchRequest?): Page<CirclePageResponse?>
}