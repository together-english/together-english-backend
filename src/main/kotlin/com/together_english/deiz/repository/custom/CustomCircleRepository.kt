package com.together_english.deiz.repository.custom

import com.together_english.deiz.model.circle.dto.CircleDetailResponse
import com.together_english.deiz.model.circle.dto.CirclePageResponse
import com.together_english.deiz.model.circle.dto.CircleSearchRequest
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface CustomCircleRepository {
    fun findCirclesByPagination(pageable: Pageable, request: CircleSearchRequest?): Page<CirclePageResponse?>
}