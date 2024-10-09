package com.together_english.deiz.model.circle

import java.io.Serializable

data class FavoriteCircleId(
        val circle: Long = 0,
        val member: Long = 0
) : Serializable