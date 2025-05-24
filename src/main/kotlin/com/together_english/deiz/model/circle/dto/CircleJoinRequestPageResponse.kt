package com.together_english.deiz.model.circle.dto

data class CircleJoinRequestPageResponse(
    val content: List<CircleJoinRequestDto>,
    val page: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int
)