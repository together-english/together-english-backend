package com.together_english.deiz.repository.custom

import com.together_english.deiz.model.circle.dto.CircleJoinDetailResponse
import java.util.*

interface CustomCircleJoinRepository {
    fun findCircleJoinRequestDetail(circleJoinRequestId: UUID): CircleJoinDetailResponse?
}